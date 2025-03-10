package org.daylight.routenavigator.backend.model.incoming;

//import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * An incoming structure representing a request to book a route
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class BookRouteRequest {
    @NotNull
    private long routeId;

    @NotNull
    private int ticketsAmount;
}
