package org.daylight.routenavigator.backend.utility;

import org.daylight.routenavigator.backend.model.incoming.RouteSearchRequest;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component("routeKeyGenerator")
public class RouteCacheKeyGenerator implements KeyGenerator {
    private static final String NULL_VALUE = "NULL";
    private static final String EMPTY_VALUE = "EMPTY";

    @Override
    public Object generate(Object target, Method method, Object... params) {
        RouteSearchRequest request = (RouteSearchRequest) params[0];
        return String.format(
                "loc:%s-%s|time:%s|types:%s|price:%d-%d|limit:%d",
                normalizeString(request.getDepartureLocation()),
                normalizeString(request.getArrivalLocation()),
                normalizeTime(request.getDepartureTimeMin()),
                normalizeTransportTypes(request.getTransportAllowed()),
                request.getMinPrice(),
                request.getMaxPrice(),
                request.getAmountLimit()
        );
    }

    private String normalizeString(String value) {
        if (value == null) return NULL_VALUE;
        if (value.trim().isEmpty()) return EMPTY_VALUE;
        return value.trim().toLowerCase();
    }

    private String normalizeTime(String time) {
        if (time == null) return NULL_VALUE;
        try {
            return OffsetDateTime.parse(time)
                    .truncatedTo(ChronoUnit.MINUTES)
                    .toString();
        } catch (Exception e) {
            return "INVALID_TIME";
        }
    }

    private String normalizeTransportTypes(List<String> types) {
        if (types == null) return NULL_VALUE;
        if (types.isEmpty()) return EMPTY_VALUE;

        return types.stream()
                .map(this::normalizeString)
                .distinct()
                .sorted()
                .collect(Collectors.joining(","));
    }
}
