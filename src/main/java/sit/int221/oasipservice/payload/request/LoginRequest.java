package sit.int221.oasipservice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginRequest {
    @NotNull
    @NotEmpty
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    @Size(min = 1, max = 50)
    private String userEmail;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^\\S*$", message = "white spaces do not allowed in the entire string")
    @Size(min = 8, max = 14)
    private String userPassword;
}
