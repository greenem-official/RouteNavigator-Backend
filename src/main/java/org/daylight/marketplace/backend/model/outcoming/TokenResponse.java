package org.daylight.marketplace.backend.model.outcoming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.daylight.marketplace.backend.entities.Token;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class TokenResponse {
    private UUID token;
    private OffsetDateTime expires;

    public TokenResponse(Token tokenObject) {
        this.token = tokenObject.getToken();
        this.expires = tokenObject.getExpires();
    }
}
