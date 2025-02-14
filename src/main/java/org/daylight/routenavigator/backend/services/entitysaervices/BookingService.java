package org.daylight.routenavigator.backend.services.entitysaervices;

import org.daylight.routenavigator.backend.entities.Booking;
import org.daylight.routenavigator.backend.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    public void save(Booking booking) {
        bookingRepository.save(booking);
    }

    public List<Booking> findAllByUserId(long userId) {
        return bookingRepository.findAllByUserId(userId);
    }

    public List<Booking> findAllByRouteId(long routeId) {
        return bookingRepository.findAllByRouteId(routeId);
    }
}
