package org.daylight.marketplace.backend.services;

import org.daylight.marketplace.backend.entities.Booking;
import org.daylight.marketplace.backend.entities.Route;
import org.daylight.marketplace.backend.repositories.BookingRepository;
import org.daylight.marketplace.backend.repositories.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
