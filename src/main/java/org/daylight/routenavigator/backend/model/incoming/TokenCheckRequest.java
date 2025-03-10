package org.daylight.routenavigator.backend.model.incoming;

//import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * An incoming structure representing a request to check if an authorization token is valid
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class TokenCheckRequest {
    @NotNull
    private String token;
}
