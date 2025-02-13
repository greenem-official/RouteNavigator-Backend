package org.daylight.marketplace.backend.services;

import org.daylight.marketplace.backend.entities.Location;
import org.daylight.marketplace.backend.entities.TransportType;
import org.daylight.marketplace.backend.repositories.LocationRepository;
import org.daylight.marketplace.backend.repositories.TransportTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransportTypeService {
    @Autowired
    private TransportTypeRepository transportTypeRepository;

    public void save(TransportType transportType) {
        transportTypeRepository.save(transportType);
    }

    public void deleteAll() {
        transportTypeRepository.deleteAll();
    }

    public Optional<TransportType> findByCode(String code) {
        return transportTypeRepository.findByCode(code);
    }

    public Optional<TransportType> findByDisplayName(String displayName) {
        return transportTypeRepository.findByDisplayName(displayName);
    }
}
