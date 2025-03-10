package org.daylight.routenavigator.backend.services.entityservices;

import org.daylight.routenavigator.backend.entities.Location;
import org.daylight.routenavigator.backend.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * A service for the Locations repository
 */
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
        return locationRepository.findByCode(code.toLowerCase());
    }

    public Optional<Location> findByDisplayName(String displayName) {
        return locationRepository.findByDisplayName(displayName);
    }

    public List<Location> findMatching(String text, int limit) {
        return locationRepository.findByDisplayNameStartingWith(text, PageRequest.of(0, limit));
    }
}
