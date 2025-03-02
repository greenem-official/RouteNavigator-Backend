package org.daylight.routenavigator.backend.repositories;

import org.daylight.routenavigator.backend.entities.Location;
import org.daylight.routenavigator.backend.entities.Route;
import org.daylight.routenavigator.backend.entities.TransportType;
import org.daylight.routenavigator.backend.model.incoming.RouteSearchRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface RouteRepository extends CrudRepository<Route, Long> {
    List<Route> findAllByDepartureLocation(Location departureLocation);

    List<Route> findAllByArrivalLocation(Location arrivalLocation);

    @Query("SELECT r FROM Route r WHERE r.departureTime >= :departureTime ORDER BY r.departureTime ASC")
    List<Route> findAllByDepartureTimeAfter(OffsetDateTime departureTime);

    @Query("SELECT r FROM Route r WHERE r.arrivalTime >= :arrivalTime ORDER BY r.arrivalTime ASC")
    List<Route> findAllByArrivalTimeAfter(OffsetDateTime arrivalTime);

    @Query("SELECT r FROM Route r WHERE r.departureLocation = :departureLocation and r.departureTime >= :departureTime ORDER BY r.departureTime ASC")
    List<Route> findAllByDepartureLocationAndDepartureTimeAfter(Location departureLocation, OffsetDateTime departureTime);

    @Query("SELECT r FROM Route r WHERE r.arrivalLocation = :arrivalLocation and r.arrivalTime <= :arrivalTime ORDER BY r.departureTime ASC")
    List<Route> findAllByArrivalLocationAndArrivalTimeBefore(Location arrivalLocation, OffsetDateTime arrivalTime);

    @Query("SELECT r " +
            "FROM Route r " +
            "WHERE (:departureLocation IS NULL OR r.departureLocation = :departureLocation) " +
            "AND (:arrivalLocation IS NULL OR r.arrivalLocation = :arrivalLocation) " +
            "AND r.departureTime BETWEEN :departureTimeMin AND :departureTimeMax " +
            "AND r.transportType IN :transportAllowed " +
            "AND r.price BETWEEN :minPrice AND :maxPrice " +
            "ORDER BY r.departureTime ASC")
    List<Route> findAllByRequest(
            @Param("departureLocation") Location departureLocation,
            @Param("arrivalLocation") Location arrivalLocation,
            @Param("departureTimeMin") OffsetDateTime departureTimeMin,
            @Param("departureTimeMax") OffsetDateTime departureTimeMax,
            @Param("maxTravelDuration") String maxTravelDuration,
            @Param("transportAllowed") List<TransportType> transportAllowed,
            @Param("minPrice") int minPrice,
            @Param("maxPrice") int maxPrice,
            Pageable pageable);

    @Query("SELECT DISTINCT TO_CHAR(DATE_TRUNC('day', r.departureTime), 'YYYY-MM-DD') " +
            "FROM Route r " +
            "WHERE (:departureLocation IS NULL OR r.departureLocation = :departureLocation) " +
            "AND (:arrivalLocation IS NULL OR r.arrivalLocation = :arrivalLocation) " +
            "AND r.departureTime BETWEEN :departureTimeMin AND :departureTimeMax " +
            "AND r.transportType IN :transportAllowed " +
            "AND r.price BETWEEN :minPrice AND :maxPrice ")
    List<String> findAllDatesByRequest(@Param("departureLocation") Location departureLocation,
                                       @Param("arrivalLocation") Location arrivalLocation,
                                       @Param("departureTimeMin") OffsetDateTime departureTimeMin,
                                       @Param("departureTimeMax") OffsetDateTime departureTimeMax,
                                       @Param("maxTravelDuration") String maxTravelDuration,
                                       @Param("transportAllowed") List<TransportType> transportAllowed,
                                       @Param("minPrice") int minPrice,
                                       @Param("maxPrice") int maxPrice,
                                       Pageable pageable);
}
