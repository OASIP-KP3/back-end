package sit.int221.oasipservice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.oasipservice.annotations.IsRole;
import sit.int221.oasipservice.annotations.UniqueEmail;
import sit.int221.oasipservice.annotations.UniqueUsername;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Locale;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterRequest {
    private Integer id;

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 100)
    @UniqueUsername
    private String userName;

    @NotNull
    @NotEmpty
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    @Size(min = 1, max = 50)
    @UniqueEmail
    private String userEmail;

    @NotNull
    @NotEmpty
    @Size(min = 8, max = 14)
    private String userPassword;

    @NotNull
    @NotEmpty
    @IsRole
    private String userRole;

    public RegisterRequest setUserName(String userName) {
        this.userName = userName.trim();
        return this;
    }

    public RegisterRequest setUserRole(String userRole) {
        this.userRole = userRole.trim().toLowerCase(Locale.US);
        return this;
    }

    public RegisterRequest setUserEmail(String userEmail) {
        this.userEmail = userEmail.toLowerCase(Locale.US);
        return this;
    }

    public RegisterRequest setUserPassword(String userPassword) {
        this.userPassword = userPassword.trim();
        return this;
    }
}
