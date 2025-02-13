package org.daylight.marketplace.backend.repositories;

import org.daylight.marketplace.backend.entities.Booking;
import org.daylight.marketplace.backend.entities.Route;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
    List<Booking> findAllByUserId(long userId);
    List<Booking> findAllByRouteId(long routeId);
}
