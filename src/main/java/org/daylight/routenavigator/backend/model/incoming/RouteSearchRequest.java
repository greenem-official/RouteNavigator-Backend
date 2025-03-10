package org.daylight.routenavigator.backend.model.incoming;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An incoming structure representing a route search request with specified parameters
 * or a request to chech available days, or a request that only asks for a single day
 */
@Data
@Accessors(chain = true)
public class RouteSearchRequest {
    @NotNull
    String departureLocation = "";

    @NotNull
    String arrivalLocation = "";

    @NotNull
    String departureTimeMin;

    @NotNull
    int fetchDays = -1; // -1 for default

    @NotNull
    boolean fetchAvailability = false; // true if should return only info of what dates have routes

    @NotNull
    List<String> transportAllowed = new ArrayList<>();

    @NotNull
    String departureTimeMax = "2100-12-31T23:59:59+03:00"; // OffsetDateTime.of(2100, 12, 31, 23, 59, 59, 0, ZoneOffset.ofHours(3));;
    //    OffsetDateTime arrivalTimeMax = OffsetDateTime.MAX;

    @NotNull
    String maxTravelDuration = "7 days"; // Duration.ofDays(7);

    @NotNull
    @Range(min = 1, max = 20)
    int amountLimit = 20;

    @NotNull
    int minPrice = 0;

    @NotNull
    int maxPrice = Integer.MAX_VALUE;
}
