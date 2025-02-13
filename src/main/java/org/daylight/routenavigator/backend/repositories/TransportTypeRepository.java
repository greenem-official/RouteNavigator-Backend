package org.daylight.routenavigator.backend.repositories;

import org.daylight.routenavigator.backend.entities.TransportType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransportTypeRepository extends CrudRepository<TransportType, Long> {
    Optional<TransportType> findByCode(String code);
    Optional<TransportType> findByDisplayName(String displayName);
}
