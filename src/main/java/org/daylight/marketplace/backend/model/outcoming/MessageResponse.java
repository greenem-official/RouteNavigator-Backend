package org.daylight.marketplace.backend.model.outcoming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class MessageResponse {
    private String message;
}
