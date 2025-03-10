package org.daylight.routenavigator.backend.repositories;

import org.daylight.routenavigator.backend.entities.Location;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * A repository for Locations
 */
@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {
    Optional<Location> findByCode(String code);
    Optional<Location> findByDisplayName(String displayName);
    List<Location> findByDisplayNameStartingWith(String displayName, Pageable pageable);
}
