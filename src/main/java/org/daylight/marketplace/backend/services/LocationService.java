package org.daylight.marketplace.backend.services;

import org.daylight.marketplace.backend.entities.Booking;
import org.daylight.marketplace.backend.entities.Location;
import org.daylight.marketplace.backend.repositories.BookingRepository;
import org.daylight.marketplace.backend.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    public void save(Location location) {
        locationRepository.save(location);
    }

    public void deleteAll() {
        locationRepository.deleteAll();
    }

    public Optional<Location> findByCode(String code) {
        return locationRepository.findByCode(code);
    }

    public Optional<Location> findByDisplayName(String displayName) {
        return locationRepository.findByDisplayName(displayName);
    }
}
