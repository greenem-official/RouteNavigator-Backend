package org.daylight.routenavigator.backend.services.entitysaervices;

import org.daylight.routenavigator.backend.entities.Location;
import org.daylight.routenavigator.backend.entities.Route;
import org.daylight.routenavigator.backend.entities.TransportType;
import org.daylight.routenavigator.backend.model.incoming.RouteSearchRequest;
import org.daylight.routenavigator.backend.repositories.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<Route> findAllByDepartureLocation(Location departureLocation) {
        return routeRepository.findAllByDepartureLocation(departureLocation);
    }

    public List<Route> findAllByArrivalLocation(Location arrivalLocation) {
        return routeRepository.findAllByArrivalLocation(arrivalLocation);
    }

    public List<Route> findAllByDepartureTimeAfter(OffsetDateTime departureTime) {
        return routeRepository.findAllByDepartureTimeAfter(departureTime);
    }

    public List<Route> findAllByArrivalTimeAfter(OffsetDateTime arrivalTime) {
        return routeRepository.findAllByArrivalTimeAfter(arrivalTime);
    }

    public List<Route> findAllByDepartureLocationAndDepartureTimeAfter(Location departureLocation, OffsetDateTime departureTime) {
        return routeRepository.findAllByDepartureLocationAndDepartureTimeAfter(departureLocation, departureTime);
    }

    public List<Route> findAllByArrivalLocationAndArrivalTimeBefore(Location arrivalLocation, OffsetDateTime arrivalTime) {
        return routeRepository.findAllByArrivalLocationAndArrivalTimeBefore(arrivalLocation, arrivalTime);
    }

    public List<Route> findAllByRequest(RouteSearchRequest routeSearchRequest) {
        // Finding and checking provided transport types;
        List<TransportType> transportTypes = new ArrayList<>();
        routeSearchRequest.getTransportAllowed().forEach((key, value) -> {
            if (value) {
                Optional<TransportType> transportTypeObj = transportTypeService.findByCode(key);
                if (transportTypeObj.isEmpty()) {
                    throw new IllegalArgumentException("Invalid transportType: " + key);
                }
                transportTypes.add(transportTypeObj.get());
            }
        });

        // Finding and checking provided locations
        Optional<Location> departureLocation = locationService.findByDisplayName(routeSearchRequest.getDepartureLocation());
        Optional<Location> arrivalLocation = locationService.findByDisplayName(routeSearchRequest.getArrivalLocation());

        Location departureLocationFinal = null;
        Location arrivalLocationFinal = null;

        if ((!routeSearchRequest.getDepartureLocation().isEmpty() && departureLocation.isEmpty()) ||
                (!routeSearchRequest.getArrivalLocation().isEmpty() && arrivalLocation.isEmpty())) {
            throw new IllegalArgumentException("Invalid departure or arrival location");
        }

        if (departureLocation.isPresent()) {
            departureLocationFinal = departureLocation.get();
        }
        if (arrivalLocation.isPresent()) {
            arrivalLocationFinal = arrivalLocation.get();
        }

        // If that's a single date request
        if (routeSearchRequest.isThisDateOnly()) {
            routeSearchRequest.setDepartureTimeMax(OffsetDateTime.parse(routeSearchRequest.getDepartureTimeMin())
                    .truncatedTo(ChronoUnit.DAYS)
                    .plusDays(1).toString()
            );
        }

        // Querying
        return routeRepository.findAllByRequest(
                departureLocationFinal,
                arrivalLocationFinal,
                OffsetDateTime.parse(routeSearchRequest.getDepartureTimeMin()),
                OffsetDateTime.parse(routeSearchRequest.getDepartureTimeMax()),
                routeSearchRequest.getMaxTravelDuration(),
                transportTypes,
                routeSearchRequest.getMinPrice(),
                routeSearchRequest.getMaxPrice(),
                PageRequest.of(0, Math.min(100, routeSearchRequest.getAmountLimit()))
                );
    }
}
