package org.daylight.routenavigator.backend.model.incoming;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TextSearchRequest {
    @NotNull
    String text;
}
