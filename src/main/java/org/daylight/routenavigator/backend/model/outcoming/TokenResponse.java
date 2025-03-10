package org.daylight.routenavigator.backend.model.outcoming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.daylight.routenavigator.backend.entities.Token;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * An outcoming structure representing a response including the user's current authorization data
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class TokenResponse {
    private UUID token;
    private OffsetDateTime expires;
    private String userUsername;
    private String userEmail;

    public TokenResponse(Token tokenObject) {
        this.token = tokenObject.getToken();
        this.expires = tokenObject.getExpires();
    }
}
