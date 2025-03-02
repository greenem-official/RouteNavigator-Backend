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
import java.util.List;

@Component
public class TestDataImporter implements CommandLineRunner {
    private static final boolean DO_IMPORT = false;

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
        else System.out.println("Leaving database data untouched");
    }

    public void resetAndImportData() {
        System.out.println("Resetting database data...");
        routeService.deleteAll();
        locationService.deleteAll();
        transportTypeService.deleteAll();

        DataGeneration.setRouteService(routeService);
        DataGeneration.setLocationService(locationService);
        DataGeneration.setTransportTypeService(transportTypeService);

        importTransportTypes();
        importLocations();
        importRoutes();

        System.out.println("Finished importing data");
    }

    public void importTransportTypes() {
        System.out.println("Importing transport data...");

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
    }

    public void importLocations() {
        System.out.println("Importing locations data...");

        locationService.save(new Location()
                .setCode("city_moscow")
                .setDisplayName("Москва")
        );
        locationService.save(new Location()
                .setCode("city_saint_petersburg")
                .setDisplayName("Санкт-Петербург")
        );
        locationService.save(new Location()
                .setCode("city_volgograd")
                .setDisplayName("Волгоград")
        );
        locationService.save(new Location()
                .setCode("city_novosibirsk")
                .setDisplayName("Новосибирск")
        );
        locationService.save(new Location()
                .setCode("city_yekaterinburg")
                .setDisplayName("Екатеринбург")
        );
        locationService.save(new Location()
                .setCode("city_nizhny_novgorod")
                .setDisplayName("Нижний Новгород")
        );
        locationService.save(new Location()
                .setCode("city_kazan")
                .setDisplayName("Казань")
        );
        locationService.save(new Location()
                .setCode("city_rostov_on_don")
                .setDisplayName("Ростов-на-Дону")
        );
        locationService.save(new Location()
                .setCode("city_sochi")
                .setDisplayName("Сочи")
        );
        locationService.save(new Location()
                .setCode("city_samara")
                .setDisplayName("Самара")
        );
        locationService.save(new Location()
                .setCode("city_ufa")
                .setDisplayName("Уфа")
        );
        locationService.save(new Location()
                .setCode("city_chelyabinsk")
                .setDisplayName("Челябинск")
        );
        locationService.save(new Location()
                .setCode("city_omsk")
                .setDisplayName("Омск")
        );
        locationService.save(new Location()
                .setCode("city_krasnoyarsk")
                .setDisplayName("Красноярск")
        );
        locationService.save(new Location()
                .setCode("city_vladivostok")
                .setDisplayName("Владивосток")
        );
        locationService.save(new Location()
                .setCode("city_irkutsk")
                .setDisplayName("Иркутск")
        );
        locationService.save(new Location()
                .setCode("city_khabarovsk")
                .setDisplayName("Хабаровск")
        );
        locationService.save(new Location()
                .setCode("city_krasnodar")
                .setDisplayName("Краснодар")
        );
        locationService.save(new Location()
                .setCode("city_voronezh")
                .setDisplayName("Воронеж")
        );
        locationService.save(new Location()
                .setCode("city_perm")
                .setDisplayName("Пермь")
        );
        locationService.save(new Location()
                .setCode("city_saratov")
                .setDisplayName("Саратов")
        );
        locationService.save(new Location()
                .setCode("city_tolyatti")
                .setDisplayName("Тольятти")
        );
        locationService.save(new Location()
                .setCode("city_tula")
                .setDisplayName("Тула")
        );
        locationService.save(new Location()
                .setCode("city_barnaul")
                .setDisplayName("Барнаул")
        );
        locationService.save(new Location()
                .setCode("city_tyumen")
                .setDisplayName("Тюмень")
        );
        locationService.save(new Location()
                .setCode("city_izhevsk")
                .setDisplayName("Ижевск")
        );
        locationService.save(new Location()
                .setCode("city_ulyanovsk")
                .setDisplayName("Ульяновск")
        );
        locationService.save(new Location()
                .setCode("city_vladikavkaz")
                .setDisplayName("Владикавказ")
        );
        locationService.save(new Location()
                .setCode("city_makhachkala")
                .setDisplayName("Махачкала")
        );
        locationService.save(new Location()
                .setCode("city_tomsk")
                .setDisplayName("Томск")
        );
        locationService.save(new Location()
                .setCode("city_kemerovo")
                .setDisplayName("Кемерово")
        );
        locationService.save(new Location()
                .setCode("city_naberezhnye_chelny")
                .setDisplayName("Набережные Челны")
        );
        locationService.save(new Location()
                .setCode("city_yaroslavl")
                .setDisplayName("Ярославль")
        );
        locationService.save(new Location()
                .setCode("city_ryazan")
                .setDisplayName("Рязань")
        );
        locationService.save(new Location()
                .setCode("city_astrakhan")
                .setDisplayName("Астрахань")
        );
        locationService.save(new Location()
                .setCode("city_penza")
                .setDisplayName("Пенза")
        );
        locationService.save(new Location()
                .setCode("city_lipetsk")
                .setDisplayName("Липецк")
        );
        locationService.save(new Location()
                .setCode("city_kirov")
                .setDisplayName("Киров")
        );
        locationService.save(new Location()
                .setCode("city_cheboksary")
                .setDisplayName("Чебоксары")
        );
        locationService.save(new Location()
                .setCode("city_kaliningrad")
                .setDisplayName("Калининград")
        );
        locationService.save(new Location()
                .setCode("city_murmansk")
                .setDisplayName("Мурманск")
        );
        locationService.save(new Location()
                .setCode("city_arhangelsk")
                .setDisplayName("Архангельск")
        );
        locationService.save(new Location()
                .setCode("city_surgut")
                .setDisplayName("Сургут")
        );
        locationService.save(new Location()
                .setCode("city_novokuznetsk")
                .setDisplayName("Новокузнецк")
        );
        locationService.save(new Location()
                .setCode("city_orel")
                .setDisplayName("Орёл")
        );
        locationService.save(new Location()
                .setCode("city_smolensk")
                .setDisplayName("Смоленск")
        );
        locationService.save(new Location()
                .setCode("city_tver")
                .setDisplayName("Тверь")
        );
        locationService.save(new Location()
                .setCode("city_belgorod")
                .setDisplayName("Белгород")
        );
        locationService.save(new Location()
                .setCode("city_kursk")
                .setDisplayName("Курск")
        );
        locationService.save(new Location()
                .setCode("city_ivanovo")
                .setDisplayName("Иваново")
        );
        locationService.save(new Location()
                .setCode("city_bryansk")
                .setDisplayName("Брянск")
        );
        locationService.save(new Location()
                .setCode("city_vladimir")
                .setDisplayName("Владимир")
        );
        locationService.save(new Location()
                .setCode("city_chita")
                .setDisplayName("Чита")
        );
        locationService.save(new Location()
                .setCode("city_abakan")
                .setDisplayName("Абакан")
        );
        locationService.save(new Location()
                .setCode("city_magnitogorsk")
                .setDisplayName("Магнитогорск")
        );
        locationService.save(new Location()
                .setCode("city_stavropol")
                .setDisplayName("Ставрополь")
        );
        locationService.save(new Location()
                .setCode("city_simferopol")
                .setDisplayName("Симферополь")
        );
        locationService.save(new Location()
                .setCode("city_sevastopol")
                .setDisplayName("Севастополь")
        );
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void importRoutes() {
        System.out.println("Generating routes data...");
        List<Route> routes = DataGeneration.prepareRoutesForTimeframe(
                OffsetDateTime.parse("2025-02-20 00:00:00+03:00", formatter),
                OffsetDateTime.parse("2025-04-20 00:00:00+03:00", formatter)
        );

        System.out.println("Saving routes data...");
        routeService.saveAll(routes);

//        try {
//            routeService.save(new Route()
//                    .setDepartureLocation(locationService.findByCode("city_moscow").get())
//                    .setArrivalLocation(locationService.findByCode("city_saint_petersburg").get())
//                    .setTransportType(transportTypeService.findByCode("train").get())
//                    .setDepartureTime(OffsetDateTime.parse("2025-03-20 10:30:00+03:00", formatter))
//                    .setArrivalTime(OffsetDateTime.parse("2025-03-20 17:00:00+03:00", formatter))
//            );
//
////            routeService.save(new Route()
////                    .setDepartureLocation(locationService.findByCode("city_moscow").get())
////                    .setArrivalLocation(locationService.findByCode("city_crimea").get())
////                    .setTransportType(transportTypeService.findByCode("plane").get())
////                    .setDepartureTime(OffsetDateTime.parse("2025-03-24 20:00:00+03:00", formatter))
////                    .setArrivalTime(OffsetDateTime.parse("2025-03-24 22:00:00+03:00", formatter))
////            );
//
//            routeService.save(new Route()
//                    .setDepartureLocation(locationService.findByCode("city_moscow").get())
//                    .setArrivalLocation(locationService.findByCode("city_volgograd").get())
//                    .setTransportType(transportTypeService.findByCode("bus").get())
//                    .setDepartureTime(OffsetDateTime.parse("2025-03-22 10:00:00+03:00", formatter))
//                    .setArrivalTime(OffsetDateTime.parse("2025-03-24 11:40:00+03:00", formatter))
//            );
//            routeService.save(new Route()
//                    .setDepartureLocation(locationService.findByCode("city_volgograd").get())
//                    .setArrivalLocation(locationService.findByCode("city_novosibirsk").get())
//                    .setTransportType(transportTypeService.findByCode("train").get())
//                    .setDepartureTime(OffsetDateTime.parse("2025-03-26 12:00:00+03:00", formatter))
//                    .setArrivalTime(OffsetDateTime.parse("2025-03-29 14:30:00+03:00", formatter))
//            );
//
////            routeService.save(new Route()
////                    .setDepartureLocation(locationService.findByCode("city_crimea").get())
////                    .setArrivalLocation(locationService.findByCode("city_volgograd").get())
////                    .setTransportType(transportTypeService.findByCode("train").get())
////                    .setDepartureTime(OffsetDateTime.parse("2025-03-25 02:00:00+03:00", formatter))
////                    .setArrivalTime(OffsetDateTime.parse("2025-03-25 14:00:00+03:00", formatter))
////            );
//            routeService.save(new Route()
//                    .setDepartureLocation(locationService.findByCode("city_volgograd").get())
//                    .setArrivalLocation(locationService.findByCode("city_novosibirsk").get())
//                    .setTransportType(transportTypeService.findByCode("train").get())
//                    .setDepartureTime(OffsetDateTime.parse("2025-03-25 16:00:00+03:00", formatter))
//                    .setArrivalTime(OffsetDateTime.parse("2025-03-26 11:30:00+03:00", formatter))
//            );
//
//            routeService.save(new Route()
//                    .setDepartureLocation(locationService.findByCode("city_saint_petersburg").get())
//                    .setArrivalLocation(locationService.findByCode("city_yekaterinburg").get())
//                    .setTransportType(transportTypeService.findByCode("bus").get())
//                    .setDepartureTime(OffsetDateTime.parse("2025-03-22 17:30:00+03:00", formatter))
//                    .setArrivalTime(OffsetDateTime.parse("2025-03-24 10:00:00+03:00", formatter))
//            );
//            routeService.save(new Route()
//                    .setDepartureLocation(locationService.findByCode("city_yekaterinburg").get())
//                    .setArrivalLocation(locationService.findByCode("city_rostov_on_don").get())
//                    .setTransportType(transportTypeService.findByCode("train").get())
//                    .setDepartureTime(OffsetDateTime.parse("2025-03-26 15:30:00+03:00", formatter))
//                    .setArrivalTime(OffsetDateTime.parse("2025-03-27 08:00:00+03:00", formatter))
//            );
//            routeService.save(new Route()
//                    .setDepartureLocation(locationService.findByCode("city_rostov_on_don").get())
//                    .setArrivalLocation(locationService.findByCode("city_novosibirsk").get())
//                    .setTransportType(transportTypeService.findByCode("plane").get())
//                    .setDepartureTime(OffsetDateTime.parse("2025-03-28 15:30:00+03:00", formatter))
//                    .setArrivalTime(OffsetDateTime.parse("2025-03-28 18:00:00+03:00", formatter))
//            );
//        } catch (NoSuchElementException e) {
//            System.out.println(e.getMessage());
//        }
    }
}
