package org.daylight.marketplace.testdata;

import org.daylight.marketplace.MarketplaceApplication;
import org.daylight.marketplace.backend.entities.Location;
import org.daylight.marketplace.backend.entities.Route;
import org.daylight.marketplace.backend.entities.TransportType;
import org.daylight.marketplace.backend.services.LocationService;
import org.daylight.marketplace.backend.services.RouteService;
import org.daylight.marketplace.backend.services.TransportTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TestDataImporter implements CommandLineRunner {
    @Autowired
    private RouteService routeService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private TransportTypeService transportTypeService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXXX");

    public TestDataImporter(RouteService routeService) {
        this.routeService = routeService;
    }

    @Override
    public void run(String... args) throws Exception {
        resetAndImportData();
    }

    public void resetAndImportData() {
        resetAndImportTransportTypes();
        resetAndImportLocations();
        resetAndImportRoutes();
    }

    public void resetAndImportTransportTypes() {
        transportTypeService.deleteAll();

        transportTypeService.save(new TransportType()
                .setCode("transport_train")
                .setDisplayName("Поезд")
        );

        transportTypeService.save(new TransportType()
                .setCode("transport_plane")
                .setDisplayName("Самолёт")
        );

        transportTypeService.save(new TransportType()
                .setCode("transport_bus")
                .setDisplayName("Автобус")
        );

        transportTypeService.save(new TransportType()
                .setCode("transport_ship")
                .setDisplayName("Корабль")
        );
    }

    public void resetAndImportLocations() {
        locationService.deleteAll();

        locationService.save(new Location()
                .setCode("location_msk")
                .setDisplayName("Москва")
        );

        locationService.save(new Location()
                .setCode("location_spb")
                .setDisplayName("Санкт-Петербург")
        );

        locationService.save(new Location()
                .setCode("location_crimea")
                .setDisplayName("Крым")
        );

        locationService.save(new Location()
                .setCode("location_volgograd")
                .setDisplayName("Волгоград")
        );
    }

    public void resetAndImportRoutes() {
        routeService.deleteAll();

        routeService.save(new Route()
                .setDepartureLocation(locationService.findByCode("location_msk").get())
                .setArrivalLocation(locationService.findByCode("location_spb").get())
                .setTransportType(transportTypeService.findByCode("transport_train").get())
                .setDepartureTime(OffsetDateTime.parse("2025-03-20 10:30:00+03:00", formatter))
                .setArrivalTime(OffsetDateTime.parse("2025-03-20 17:00:00+03:00", formatter))
        );
        routeService.save(new Route()
                .setDepartureLocation(locationService.findByCode("location_msk").get())
                .setArrivalLocation(locationService.findByCode("location_crimea").get())
                .setTransportType(transportTypeService.findByCode("transport_plane").get())
                .setDepartureTime(OffsetDateTime.parse("2025-03-24 20:00:00+03:00", formatter))
                .setArrivalTime(OffsetDateTime.parse("2025-03-25 02:00:00+03:00", formatter))
        );
        routeService.save(new Route()
                .setDepartureLocation(locationService.findByCode("location_msk").get())
                .setArrivalLocation(locationService.findByCode("location_volgograd").get())
                .setTransportType(transportTypeService.findByCode("transport_bus").get())
                .setDepartureTime(OffsetDateTime.parse("2025-03-22 10:00:00+03:00", formatter))
                .setArrivalTime(OffsetDateTime.parse("2025-03-22 16:40:00+03:00", formatter))
        );
    }
}
