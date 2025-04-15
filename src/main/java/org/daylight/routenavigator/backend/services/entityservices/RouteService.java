package org.daylight.routenavigator.backend.services.entityservices;

import org.daylight.routenavigator.backend.entities.Location;
import org.daylight.routenavigator.backend.entities.Route;
import org.daylight.routenavigator.backend.entities.TransportType;
import org.daylight.routenavigator.backend.model.incoming.RouteSearchRequest;
import org.daylight.routenavigator.backend.repositories.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A service for the Routes repository
 */
@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private LocationService locationService;
    @Autowired
    private TransportTypeService transportTypeService;

    public void save(Route route) {
        routeRepository.save(route);
    }

    public void deleteAll() {
        routeRepository.deleteAll();
    }

    public void saveAll(List<Route> routes) {
        routeRepository.saveAll(routes);
    }

    public Optional<Route> findById(long id) {
        return routeRepository.findById(id);
    }

    public List<Route> findAllByDepartureTimeAfter(OffsetDateTime departureTime) {
        return routeRepository.findAllByDepartureTimeAfter(departureTime, OffsetDateTime.now());
    }

    public List<Route> findAllByArrivalTimeAfter(OffsetDateTime arrivalTime) {
        return routeRepository.findAllByArrivalTimeAfter(arrivalTime, OffsetDateTime.now());
    }

    public List<Route> findAllByDepartureLocationAndDepartureTimeAfter(Location departureLocation, OffsetDateTime departureTime) {
        return routeRepository.findAllByDepartureLocationAndDepartureTimeAfter(departureLocation, departureTime, OffsetDateTime.now());
    }

    public List<Route> findAllByArrivalLocationAndArrivalTimeBefore(Location arrivalLocation, OffsetDateTime arrivalTime) {
        return routeRepository.findAllByArrivalLocationAndArrivalTimeBefore(arrivalLocation, arrivalTime, OffsetDateTime.now());
    }

    private List<TransportType> extractTransportTypes(RouteSearchRequest routeSearchRequest) {
        List<TransportType> transportTypes = new ArrayList<>();
        routeSearchRequest.getTransportAllowed().forEach((value) -> {
            Optional<TransportType> transportTypeObj = transportTypeService.findByCode(value);
            if (transportTypeObj.isEmpty()) {
                throw new IllegalArgumentException("Invalid transportType: " + value);
            }
            transportTypes.add(transportTypeObj.get());
        });

        return transportTypes;
    }

    private Location extractLocation(String locationData) {
        Optional<Location> location = locationService.findByDisplayName(locationData);
        if (!locationData.isEmpty() && location.isEmpty()) {
            throw new IllegalArgumentException("Invalid departure or arrival location");
        }

        Location locationResult = null;

        if (location.isPresent()) {
            locationResult = location.get();
        }

        return locationResult;
    }

    @Cacheable(
            value = "routes",
            keyGenerator = "routeKeyGenerator",
            unless = "#result == null || #result.isEmpty()"
    )
    public List<Route> findAllByRequest(RouteSearchRequest routeSearchRequest) {
        // Finding and checking provided transport types;
        List<TransportType> transportTypes = extractTransportTypes(routeSearchRequest);

        // Finding and checking provided locations or just getting a null (default)
        Location departureLocation = extractLocation(routeSearchRequest.getDepartureLocation());
        Location arrivalLocation = extractLocation(routeSearchRequest.getArrivalLocation());

        // If that's a single dates request
        if (routeSearchRequest.getFetchDays() > 0) {
            routeSearchRequest.setDepartureTimeMax(OffsetDateTime.parse(routeSearchRequest.getDepartureTimeMin())
                    .truncatedTo(ChronoUnit.DAYS)
                    .plusDays(1).toString()
            );
        }

        OffsetDateTime now = OffsetDateTime.now();

        // Querying
        return routeRepository.findAllByRequest(
                departureLocation,
                arrivalLocation,
                OffsetDateTime.parse(routeSearchRequest.getDepartureTimeMin()),
                OffsetDateTime.parse(routeSearchRequest.getDepartureTimeMax()),
                routeSearchRequest.getMaxTravelDuration(),
                transportTypes,
                routeSearchRequest.getMinPrice(),
                routeSearchRequest.getMaxPrice(),
                now,
                PageRequest.of(0, Math.min(100, routeSearchRequest.getAmountLimit()))
        ).stream()
                .filter(route -> route.getDepartureTime().isAfter(now))
                .collect(Collectors.toList());
    }

    @Cacheable(
            value = "routeDates",
            keyGenerator = "routeKeyGenerator",
            unless = "#result == null || #result.isEmpty()"
    )
    public List<String> findDatesForRequest(RouteSearchRequest routeSearchRequest) {
        // Finding and checking provided transport types;
        List<TransportType> transportTypes = extractTransportTypes(routeSearchRequest);

        // Finding and checking provided locations or just getting a null (default)
        Location departureLocation = extractLocation(routeSearchRequest.getDepartureLocation());
        Location arrivalLocation = extractLocation(routeSearchRequest.getArrivalLocation());

        routeSearchRequest.setDepartureTimeMax(
                OffsetDateTime.parse(routeSearchRequest.getDepartureTimeMin())
                        .plusDays(routeSearchRequest.getFetchDays())
                        .toString()
        );

        boolean postgresql = false;

        try {
            postgresql = isPostgreSQL();
        } catch (SQLException e) {
            System.out.println("Couldn't determine database type");
        }

        // Querying, different methods for SQLite and PostgreSQL due to different sql syntax
        List<String> result;
        if(postgresql) {
            result = routeRepository.findAllDatesByRequestPostgres(
                    departureLocation,
                    arrivalLocation,
                    OffsetDateTime.parse(routeSearchRequest.getDepartureTimeMin()),
                    OffsetDateTime.parse(routeSearchRequest.getDepartureTimeMax()),
                    routeSearchRequest.getMaxTravelDuration(),
                    transportTypes,
                    routeSearchRequest.getMinPrice(),
                    routeSearchRequest.getMaxPrice(),
                    OffsetDateTime.now(),
                    PageRequest.of(0, Math.min(100, routeSearchRequest.getAmountLimit()))
            );
        } else {
            // IMPORTANT EXPLANATION:
            // Sqlite had a lot of problems selecting distinct formatted dates, so we have to do it here and even modify the limit size.
            // The entire SQLITE version is NOT SUPPOSED TO BE USED (!!!) in production instead of postgresql.
            // it's just hard to determine if the given task allows postgresql
            List<OffsetDateTime> dates = routeRepository.findAllDatesByRequestSqlite(
                    departureLocation,
                    arrivalLocation,
                    OffsetDateTime.parse(routeSearchRequest.getDepartureTimeMin()),
                    OffsetDateTime.parse(routeSearchRequest.getDepartureTimeMax()),
                    routeSearchRequest.getMaxTravelDuration(),
                    transportTypes,
                    routeSearchRequest.getMinPrice(),
                    routeSearchRequest.getMaxPrice(),
                    OffsetDateTime.now(),
                    PageRequest.of(0, 500)
            );

//            System.out.println(dates);

            result = dates.stream()
                    .map(timestamp -> timestamp.format(findDatesFormatter))
                    .distinct()
                    .collect(Collectors.toList());
        }

//        System.out.println(result);
        return result;
    }

    private DateTimeFormatter findDatesFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static Optional<Boolean> isPostgreSQL = Optional.empty();

    public boolean isPostgreSQL() throws SQLException {
        if(isPostgreSQL.isPresent()) {
            return isPostgreSQL.get();
        }

        boolean result = false;
        try {
            DatabaseMetaData metaData = Objects.requireNonNull(jdbcTemplate.getDataSource())
                    .getConnection()
                    .getMetaData();

//            System.out.println("Database Product Name: " + metaData.getDatabaseProductName());
//            System.out.println("Database Product Version: " + metaData.getDatabaseProductVersion());
//            System.out.println("Driver Name: " + metaData.getDriverName());
//            System.out.println("Driver Version: " + metaData.getDriverVersion());
//            System.out.println("URL: " + metaData.getURL());
//            System.out.println("User Name: " + metaData.getUserName());
            result = metaData.getDatabaseProductName().equalsIgnoreCase("postgresql");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve database metadata", e);
        }

        isPostgreSQL = Optional.of(result);
        return isPostgreSQL.get();
    }
}
