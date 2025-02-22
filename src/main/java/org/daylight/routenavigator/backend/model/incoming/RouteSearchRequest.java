package org.daylight.routenavigator.backend.model.incoming;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Data
@Accessors(chain = true)
public class RouteSearchRequest {
//    @NotNull
    String departureLocation;

//    @NotNull
    String arrivalLocation;

    @NotNull
    String departureTimeMin;

    @NotNull
    boolean thisDateOnly;

    @NotNull
    Map<String, Boolean> transportAllowed;

    String departureTimeMax = "2100-12-31T23:59:59+03:00"; // OffsetDateTime.of(2100, 12, 31, 23, 59, 59, 0, ZoneOffset.ofHours(3));;
    //    OffsetDateTime arrivalTimeMax = OffsetDateTime.MAX;
    String maxTravelDuration = "7 days"; // Duration.ofDays(7);

    @Range(min = 1, max = 20)
    int amountLimit = 20;

    int minPrice = 0;
    int maxPrice = Integer.MAX_VALUE;
}
