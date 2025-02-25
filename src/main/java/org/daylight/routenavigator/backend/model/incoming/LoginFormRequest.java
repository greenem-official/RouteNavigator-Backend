package org.daylight.routenavigator.backend.model.incoming;

//import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class LoginFormRequest {
    @NotNull
    private String email;

    @Size(min = 6, max = 25)
    private String username;

    @NotNull
    @Size(min = 6, max = 25)
    private String password;
}
