package org.daylight.routenavigator.backend.model.incoming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * An incoming structure representing authorization data
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class AuthorizationData {
    private String token;
}
