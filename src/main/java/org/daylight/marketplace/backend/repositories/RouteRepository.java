package org.daylight.marketplace.backend.repositories;

import org.daylight.marketplace.backend.entities.Location;
import org.daylight.marketplace.backend.entities.Route;
import org.daylight.marketplace.backend.entities.Token;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends CrudRepository<Route, Long> {
    List<Route> findAllByDepartureLocation(Location departureLocation);

    List<Route> findAllByArrivalLocation(Location arrivalLocation);

    @Query("SELECT r FROM Route r WHERE r.departureTime >= :departureTime ORDER BY r.departureTime ASC")
    List<Route> findAllByDepartureTimeAfter(OffsetDateTime departureTime);

    @Query("SELECT r FROM Route r WHERE r.arrivalTime >= :arrivalTime ORDER BY r.arrivalTime ASC")
    List<Route> findAllByArrivalTimeAfter(OffsetDateTime arrivalTime);
}
