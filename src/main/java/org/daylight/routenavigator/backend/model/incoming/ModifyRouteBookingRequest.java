package org.daylight.routenavigator.backend.model.incoming;

//import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class ModifyRouteBookingRequest {
    @NotNull
    private long bookingId;

    @NotNull
    @Range(min = 0, max = 100)
    private int setTicketsAmount;
}
