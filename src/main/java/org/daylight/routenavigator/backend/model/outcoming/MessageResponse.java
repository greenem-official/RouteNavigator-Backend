package org.daylight.routenavigator.backend.model.outcoming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * An outcoming structure representing a simple message REST API response
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class MessageResponse {
    private String message;
}
