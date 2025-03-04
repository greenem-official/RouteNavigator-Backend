package org.daylight.routenavigator.backend.services.entitysaervices;

import org.daylight.routenavigator.backend.entities.Booking;
import org.daylight.routenavigator.backend.entities.Route;
import org.daylight.routenavigator.backend.entities.User;
import org.daylight.routenavigator.backend.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    public void save(Booking booking) {
        bookingRepository.save(booking);
    }

    public void delete(Booking booking) {
        bookingRepository.delete(booking);
    }

    public Optional<Booking> findById(long bookingId) {
        return bookingRepository.findById(bookingId);
    }

    public List<Booking> findAllByUser(User user) {
        return bookingRepository.findAllByUserOrderByBookedAtDesc(user);
    }

    public List<Booking> findAllByRoute(Route route) {
        return bookingRepository.findAllByRoute(route);
    }
}
