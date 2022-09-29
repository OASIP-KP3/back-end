package sit.int221.oasipservice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.oasipservice.annotations.IsRole;
import sit.int221.oasipservice.annotations.UniqueEmail;
import sit.int221.oasipservice.annotations.UniqueUsername;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterRequest {
    private Integer id;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^\\S*$", message = "white spaces do not allowed in the entire string")
    @Size(min = 1, max = 100)
    @UniqueUsername
    private String userName;

    @NotNull
    @NotEmpty
    @Email(regexp = "^(?=.{1,50}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    @Size(min = 1, max = 50)
    @UniqueEmail
    private String userEmail;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^\\S*$", message = "white spaces do not allowed in the entire string")
    @Size(min = 8, max = 14)
    private String userPassword;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^\\S*$", message = "white spaces do not allowed in the entire string")
    @IsRole
    private String userRole;
}
