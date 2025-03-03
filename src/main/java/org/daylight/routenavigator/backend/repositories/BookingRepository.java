package org.daylight.routenavigator.backend.repositories;

import org.daylight.routenavigator.backend.entities.Booking;
import org.daylight.routenavigator.backend.entities.Route;
import org.daylight.routenavigator.backend.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
    List<Booking> findAllByUserOrderByBookedAtDesc(User user);
    List<Booking> findAllByRoute(Route route);
}
