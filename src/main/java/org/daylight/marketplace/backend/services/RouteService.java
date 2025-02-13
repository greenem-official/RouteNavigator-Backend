package org.daylight.marketplace.backend.services;

import org.daylight.marketplace.backend.entities.Location;
import org.daylight.marketplace.backend.entities.Route;
import org.daylight.marketplace.backend.entities.Token;
import org.daylight.marketplace.backend.repositories.RouteRepository;
import org.daylight.marketplace.backend.repositories.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;

    public void save(Route route) {
        routeRepository.save(route);
    }

    public void deleteAll() {
        routeRepository.deleteAll();
    }

    public List<Route> findAllByDepartureLocation(Location departureLocation) {
        return routeRepository.findAllByDepartureLocation(departureLocation);
    }

    public List<Route> findAllByArrivalLocation(Location arrivalLocation) {
        return routeRepository.findAllByArrivalLocation(arrivalLocation);
    }

    public List<Route> findAllByDepartureTimeAfter(OffsetDateTime departureTime) {
        return routeRepository.findAllByDepartureTimeAfter(departureTime);
    }

    public List<Route> findAllByArrivalTimeAfter(OffsetDateTime arrivalTime) {
        return routeRepository.findAllByArrivalTimeAfter(arrivalTime);
    }
}
