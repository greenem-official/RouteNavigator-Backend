package org.daylight.routenavigator.backend.model.incoming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class AuthorizationData {
    private String token;
}
