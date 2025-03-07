package org.daylight.routenavigator.testdata;

import org.daylight.routenavigator.backend.entities.Location;
import org.daylight.routenavigator.backend.entities.Route;
import org.daylight.routenavigator.backend.services.entityservices.LocationService;
import org.daylight.routenavigator.backend.services.entityservices.RouteService;
import org.daylight.routenavigator.backend.services.entityservices.TransportTypeService;

import java.time.OffsetDateTime;
import java.util.*;

public class DataGeneration {
    private static RouteService routeService;
    private static LocationService locationService;
    private static TransportTypeService transportTypeService;

    private static final Random random = new Random(1000);

    public static void setRouteService(RouteService routeService) {
        DataGeneration.routeService = routeService;
    }

    public static void setLocationService(LocationService locationService) {
        DataGeneration.locationService = locationService;
    }

    public static void setTransportTypeService(TransportTypeService transportTypeService) {
        DataGeneration.transportTypeService = transportTypeService;
    }

    private static final Map<String, Integer> locationTravelFrequency = new HashMap<>();

    static {
        locationTravelFrequency.put("city_moscow", 14); // Москва
        locationTravelFrequency.put("city_saint_petersburg", 12); // Санкт-Петербург
        locationTravelFrequency.put("city_volgograd", 10); // Волгоград
        locationTravelFrequency.put("city_novosibirsk", 11); // Новосибирск
        locationTravelFrequency.put("city_yekaterinburg", 10); // Екатеринбург
        locationTravelFrequency.put("city_nizhny_novgorod", 9); // Нижний Новгород
        locationTravelFrequency.put("city_kazan", 9); // Казань
        locationTravelFrequency.put("city_rostov_on_don", 8); // Ростов-на-Дону
        locationTravelFrequency.put("city_sochi", 8); // Сочи
        locationTravelFrequency.put("city_samara", 8); // Самара
        locationTravelFrequency.put("city_ufa", 7); // Уфа
        locationTravelFrequency.put("city_chelyabinsk", 7); // Челябинск
        locationTravelFrequency.put("city_omsk", 7); // Омск
        locationTravelFrequency.put("city_krasnoyarsk", 6); // Красноярск
        locationTravelFrequency.put("city_vladivostok", 6); // Владивосток
        locationTravelFrequency.put("city_irkutsk", 6); // Иркутск
        locationTravelFrequency.put("city_khabarovsk", 5); // Хабаровск
        locationTravelFrequency.put("city_krasnodar", 7); // Краснодар
        locationTravelFrequency.put("city_voronezh", 6); // Воронеж
        locationTravelFrequency.put("city_perm", 6); // Пермь
        locationTravelFrequency.put("city_saratov", 5); // Саратов
        locationTravelFrequency.put("city_tolyatti", 5); // Тольятти
        locationTravelFrequency.put("city_tula", 5); // Тула
        locationTravelFrequency.put("city_barnaul", 4); // Барнаул
        locationTravelFrequency.put("city_tyumen", 5); // Тюмень
        locationTravelFrequency.put("city_izhevsk", 4); // Ижевск
        locationTravelFrequency.put("city_ulyanovsk", 4); // Ульяновск
        locationTravelFrequency.put("city_vladikavkaz", 3); // Владикавказ
        locationTravelFrequency.put("city_makhachkala", 3); // Махачкала
        locationTravelFrequency.put("city_tomsk", 4); // Томск
        locationTravelFrequency.put("city_kemerovo", 4); // Кемерово
        locationTravelFrequency.put("city_naberezhnye_chelny", 4); // Набережные Челны
        locationTravelFrequency.put("city_yaroslavl", 5); // Ярославль
        locationTravelFrequency.put("city_ryazan", 4); // Рязань
        locationTravelFrequency.put("city_astrakhan", 4); // Астрахань
        locationTravelFrequency.put("city_penza", 4); // Пенза
        locationTravelFrequency.put("city_lipetsk", 3); // Липецк
        locationTravelFrequency.put("city_kirov", 3); // Киров
        locationTravelFrequency.put("city_cheboksary", 3); // Чебоксары
        locationTravelFrequency.put("city_kaliningrad", 5); // Калининград
        locationTravelFrequency.put("city_murmansk", 3); // Мурманск
        locationTravelFrequency.put("city_arhangelsk", 3); // Архангельск
        locationTravelFrequency.put("city_surgut", 4); // Сургут
        locationTravelFrequency.put("city_novokuznetsk", 3); // Новокузнецк
        locationTravelFrequency.put("city_orel", 3); // Орёл
        locationTravelFrequency.put("city_smolensk", 3); // Смоленск
        locationTravelFrequency.put("city_tver", 3); // Тверь
        locationTravelFrequency.put("city_belgorod", 3); // Белгород
        locationTravelFrequency.put("city_kursk", 3); // Курск
        locationTravelFrequency.put("city_ivanovo", 2); // Иваново
        locationTravelFrequency.put("city_bryansk", 2); // Брянск
        locationTravelFrequency.put("city_vladimir", 2); // Владимир
        locationTravelFrequency.put("city_chita", 2); // Чита
        locationTravelFrequency.put("city_abakan", 2); // Абакан
        locationTravelFrequency.put("city_magnitogorsk", 3); // Магнитогорск
        locationTravelFrequency.put("city_stavropol", 3); // Ставрополь
        locationTravelFrequency.put("city_simferopol", 4); // Симферополь
        locationTravelFrequency.put("city_sevastopol", 4); // Севастополь
    }

    private static final List<String> locationsList = new ArrayList<>();
    static {
        locationTravelFrequency.forEach((k, v) -> locationsList.add(k));
    }

    private static final List<String> transportTypes = Arrays.asList("transport_plane", "transport_train", "transport_bus");
    private static final List<Double> transportTypeWeights = Arrays.asList(5d, 7d, 4d);

    private static final Map<String, Float> averageTransportTravelDuration = new HashMap<>();
    static {
        averageTransportTravelDuration.put("transport_plane", 2.5f);
        averageTransportTravelDuration.put("transport_train", 10f);
        averageTransportTravelDuration.put("transport_bus", 5f);
    }

    private static final Map<String, Float> averageTransportTravelRandomness = new HashMap<>();
    static {
        averageTransportTravelRandomness.put("transport_plane", 0.3f);
        averageTransportTravelRandomness.put("transport_train", 0.4f);
        averageTransportTravelRandomness.put("transport_bus", 0.4f);
    }

    private static final Map<String, Integer> averageTransportPrice = new HashMap<>();
    static {
        averageTransportPrice.put("transport_plane", 4000);
        averageTransportPrice.put("transport_train", 2500);
        averageTransportPrice.put("transport_bus", 1500);
    }

    private static final Map<String, Float> averageTransportPriceRandomness = new HashMap<>();
    static {
        averageTransportPriceRandomness.put("transport_plane", 0.4f);
        averageTransportPriceRandomness.put("transport_train", 0.5f);
        averageTransportPriceRandomness.put("transport_bus", 0.5f);
    }

    public static float applyRandomness(float value, float randomness) {
        if (randomness < 0 || randomness > 1) {
            throw new IllegalArgumentException("Randomness should be in range of [0, 1]");
        }

        float lowerBound = value - value * randomness/2;
        float upperBound = value + value * randomness/2;

        return lowerBound + random.nextFloat() * (upperBound - lowerBound);
    }

    public static <T> T getWeightedRandom(List<T> values, List<Double> weights) {
        if (values == null || weights == null || values.size() != weights.size()) {
            throw new IllegalArgumentException("Values or weights provided are null or have different size");
        }

        double[] cumulativeWeights = new double[weights.size()];
        double totalWeight = 0.0;

        for (int i = 0; i < weights.size(); i++) {
            if (weights.get(i) < 0) {
                throw new IllegalArgumentException("Weights can't be negative");
            }
            totalWeight += weights.get(i);
            cumulativeWeights[i] = totalWeight;
        }

        Random random = new Random();
        double randomValue = random.nextDouble() * totalWeight;

        for (int i = 0; i < cumulativeWeights.length; i++) {
            if (randomValue < cumulativeWeights[i]) {
                return values.get(i);
            }
        }

        return null;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private static List<Route> prepareRoutesFor(Location firstLocation, Location secondLocation, OffsetDateTime start, OffsetDateTime end) {
        List<Route> routes = new ArrayList<>();
        int travelFrequency = locationTravelFrequency.get(firstLocation.getCode());

        int averageHoursTimespanBetween = (int) (14 * 24 * 2.3f) / travelFrequency;
        float timespanRandomness = 0.2f;

        OffsetDateTime currentTime = start;
        while (currentTime.isBefore(end)) {
            boolean incoming = random.nextBoolean();

            int timespanHours = (int) applyRandomness(averageHoursTimespanBetween, timespanRandomness);
            currentTime = currentTime.plusHours(timespanHours);
            String transportType = getWeightedRandom(transportTypes, transportTypeWeights);
            float hoursDuration = applyRandomness(
                    averageTransportTravelDuration.get(transportType),
                    averageTransportTravelRandomness.get(transportType)
            );

            int price = (int) applyRandomness(
                    averageTransportPrice.get(transportType),
                    averageTransportPriceRandomness.get(transportType)
            );

            price = (price / 100) * 100;

            assert transportType != null;

            Route route = new Route()
                    .setDepartureTime(currentTime)
                    .setArrivalTime(currentTime.plusHours((int) hoursDuration))
                    .setTransportType(transportTypeService.findByCode(transportType).get())
                    .setPrice(price);

            if (incoming) {
                route.setDepartureLocation(secondLocation);
                route.setArrivalLocation(firstLocation);
            } else {
                route.setDepartureLocation(firstLocation);
                route.setArrivalLocation(secondLocation);
            }

            routes.add(route);
        }

        return routes;
    }

    public static List<Route> prepareRoutesForTimeframe(OffsetDateTime start, OffsetDateTime end) {
        List<Route> routes = new ArrayList<>();

        for (int i = 0; i < locationsList.size(); i++) {
            for (int j = i + 1; j < locationsList.size(); j++) {
                String location1 = locationsList.get(i);
                String location2 = locationsList.get(j);

                if (!location1.equals(location2)) {
                    Location actualLocation1 = locationService.findByCode(location1).get();
                    Location actualLocation2 = locationService.findByCode(location2).get();

                    routes.addAll(prepareRoutesFor(actualLocation1, actualLocation2, start, end));
                }
            }
        }

        return routes;
    }
}
