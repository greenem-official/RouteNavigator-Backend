package org.daylight.marketplace.backend.model.incoming;

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

    @NotNull
    @Size(min = 6, max = 25)
    private String password;
}
