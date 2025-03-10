package org.daylight.routenavigator.backend.model.outcoming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * An outcoming structure representing an error REST API response
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class ErrorResponse {
    private String error;
}
