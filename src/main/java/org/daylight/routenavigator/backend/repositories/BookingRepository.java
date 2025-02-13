package org.daylight.routenavigator.backend.repositories;

import org.daylight.routenavigator.backend.entities.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
    List<Booking> findAllByUserId(long userId);
    List<Booking> findAllByRouteId(long routeId);
}
