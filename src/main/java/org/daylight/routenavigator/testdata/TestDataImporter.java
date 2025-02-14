package org.daylight.routenavigator.testdata;

import org.daylight.routenavigator.backend.entities.Location;
import org.daylight.routenavigator.backend.entities.Route;
import org.daylight.routenavigator.backend.entities.TransportType;
import org.daylight.routenavigator.backend.services.entitysaervices.LocationService;
import org.daylight.routenavigator.backend.services.entitysaervices.RouteService;
import org.daylight.routenavigator.backend.services.entitysaervices.TransportTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static org.daylight.routenavigator.constants.TimeContstants.formatter;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

@Component
public class TestDataImporter implements CommandLineRunner {
    private static final boolean DO_IMPORT = true;

    @Autowired
    private RouteService routeService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private TransportTypeService transportTypeService;

    public TestDataImporter(RouteService routeService) {
        this.routeService = routeService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (DO_IMPORT) resetAndImportData();
    }

    public void resetAndImportData() {
        System.out.println("Importing data...");

        resetAndImportTransportTypes();
        resetAndImportLocations();
        resetAndImportRoutes();

        System.out.println("Finished importing data");
    }

    public void resetAndImportTransportTypes() {
        transportTypeService.deleteAll();

        transportTypeService.save(new TransportType()
                .setCode("train")
                .setDisplayName("Поезд")
        );

        transportTypeService.save(new TransportType()
                .setCode("plane")
                .setDisplayName("Самолёт")
        );

        transportTypeService.save(new TransportType()
                .setCode("bus")
                .setDisplayName("Автобус")
        );

        transportTypeService.save(new TransportType()
                .setCode("ship")
                .setDisplayName("Корабль")
        );
    }

    public void resetAndImportLocations() {
        locationService.deleteAll();

        locationService.save(new Location()
                .setCode("msk")
                .setDisplayName("Москва")
        );
        locationService.save(new Location()
                .setCode("spb")
                .setDisplayName("Санкт-Петербург")
        );
        locationService.save(new Location()
                .setCode("crimea")
                .setDisplayName("Крым")
        );
        locationService.save(new Location()
                .setCode("volgograd")
                .setDisplayName("Волгоград")
        );
        locationService.save(new Location()
                .setCode("novosibirsk")
                .setDisplayName("Новосибирск")
        );
        locationService.save(new Location()
                .setCode("yekaterinburg")
                .setDisplayName("Екатеринбург")
        );
        locationService.save(new Location()
                .setCode("nizhny novgorod")
                .setDisplayName("Нижний Новгород")
        );
        locationService.save(new Location()
                .setCode("kazan")
                .setDisplayName("Казань")
        );
        locationService.save(new Location()
                .setCode("rostov-on-don")
                .setDisplayName("Ростов-на-Дону")
        );
        locationService.save(new Location()
                .setCode("sochi")
                .setDisplayName("Сочи")
        );
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void resetAndImportRoutes() {
        routeService.deleteAll();

        try {
            routeService.save(new Route()
                    .setDepartureLocation(locationService.findByCode("msk").get())
                    .setArrivalLocation(locationService.findByCode("spb").get())
                    .setTransportType(transportTypeService.findByCode("train").get())
                    .setDepartureTime(OffsetDateTime.parse("2025-03-20 10:30:00+03:00", formatter))
                    .setArrivalTime(OffsetDateTime.parse("2025-03-20 17:00:00+03:00", formatter))
            );

            routeService.save(new Route()
                    .setDepartureLocation(locationService.findByCode("msk").get())
                    .setArrivalLocation(locationService.findByCode("crimea").get())
                    .setTransportType(transportTypeService.findByCode("plane").get())
                    .setDepartureTime(OffsetDateTime.parse("2025-03-24 20:00:00+03:00", formatter))
                    .setArrivalTime(OffsetDateTime.parse("2025-03-24 22:00:00+03:00", formatter))
            );

            routeService.save(new Route()
                    .setDepartureLocation(locationService.findByCode("msk").get())
                    .setArrivalLocation(locationService.findByCode("volgograd").get())
                    .setTransportType(transportTypeService.findByCode("bus").get())
                    .setDepartureTime(OffsetDateTime.parse("2025-03-22 10:00:00+03:00", formatter))
                    .setArrivalTime(OffsetDateTime.parse("2025-03-24 11:40:00+03:00", formatter))
            );
            routeService.save(new Route()
                    .setDepartureLocation(locationService.findByCode("volgograd").get())
                    .setArrivalLocation(locationService.findByCode("novosibirsk").get())
                    .setTransportType(transportTypeService.findByCode("train").get())
                    .setDepartureTime(OffsetDateTime.parse("2025-03-26 12:00:00+03:00", formatter))
                    .setArrivalTime(OffsetDateTime.parse("2025-03-29 14:30:00+03:00", formatter))
            );

            routeService.save(new Route()
                    .setDepartureLocation(locationService.findByCode("crimea").get())
                    .setArrivalLocation(locationService.findByCode("volgograd").get())
                    .setTransportType(transportTypeService.findByCode("train").get())
                    .setDepartureTime(OffsetDateTime.parse("2025-03-25 02:00:00+03:00", formatter))
                    .setArrivalTime(OffsetDateTime.parse("2025-03-25 14:00:00+03:00", formatter))
            );
            routeService.save(new Route()
                    .setDepartureLocation(locationService.findByCode("volgograd").get())
                    .setArrivalLocation(locationService.findByCode("novosibirsk").get())
                    .setTransportType(transportTypeService.findByCode("train").get())
                    .setDepartureTime(OffsetDateTime.parse("2025-03-25 16:00:00+03:00", formatter))
                    .setArrivalTime(OffsetDateTime.parse("2025-03-26 11:30:00+03:00", formatter))
            );

            routeService.save(new Route()
                    .setDepartureLocation(locationService.findByCode("spb").get())
                    .setArrivalLocation(locationService.findByCode("yekaterinburg").get())
                    .setTransportType(transportTypeService.findByCode("bus").get())
                    .setDepartureTime(OffsetDateTime.parse("2025-03-22 17:30:00+03:00", formatter))
                    .setArrivalTime(OffsetDateTime.parse("2025-03-24 10:00:00+03:00", formatter))
            );
            routeService.save(new Route()
                    .setDepartureLocation(locationService.findByCode("yekaterinburg").get())
                    .setArrivalLocation(locationService.findByCode("rostov-on-don").get())
                    .setTransportType(transportTypeService.findByCode("train").get())
                    .setDepartureTime(OffsetDateTime.parse("2025-03-26 15:30:00+03:00", formatter))
                    .setArrivalTime(OffsetDateTime.parse("2025-03-27 08:00:00+03:00", formatter))
            );
            routeService.save(new Route()
                    .setDepartureLocation(locationService.findByCode("rostov-on-don").get())
                    .setArrivalLocation(locationService.findByCode("novosibirsk").get())
                    .setTransportType(transportTypeService.findByCode("plane").get())
                    .setDepartureTime(OffsetDateTime.parse("2025-03-28 15:30:00+03:00", formatter))
                    .setArrivalTime(OffsetDateTime.parse("2025-03-28 18:00:00+03:00", formatter))
            );
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }
}
