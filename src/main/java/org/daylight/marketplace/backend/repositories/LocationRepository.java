package org.daylight.marketplace.backend.repositories;

import org.daylight.marketplace.backend.entities.Booking;
import org.daylight.marketplace.backend.entities.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {
    Optional<Location> findByCode(String code);
    Optional<Location> findByDisplayName(String displayName);
}
