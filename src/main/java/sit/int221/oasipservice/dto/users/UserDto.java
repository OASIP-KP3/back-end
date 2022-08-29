package sit.int221.oasipservice.dto.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.oasipservice.annotations.IsRole;
import sit.int221.oasipservice.annotations.UniqueEmail;
import sit.int221.oasipservice.annotations.UniqueUsername;
import sit.int221.oasipservice.enumtype.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Locale;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
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
    @IsRole
    private String userRole;

    @NotEmpty
    @NotNull
    @Size(min = 8, max = 14)
    private String userPassword;

    public UserDto setUserName(String userName) {
        this.userName = userName.trim();
        return this;
    }

    public UserDto setUserEmail(String userEmail) {
        this.userEmail = userEmail.toLowerCase(Locale.US);
        return this;
    }

    public UserDto setUserRole(String userRole) {
        final String ROLE = userRole.trim().toUpperCase(Locale.US);
        this.userRole = Role.valueOf(ROLE).getRole();
        return this;
    }

    public UserDto setUserPassword(String userPassword) {
        this.userPassword = userPassword.trim();
        return this;
    }
}
