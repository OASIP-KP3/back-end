package sit.int221.oasipservice.dto.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.oasipservice.annotations.UniqueEmail;
import sit.int221.oasipservice.annotations.UniqueUsername;
import sit.int221.oasipservice.enumtype.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private Integer id;

    @NotEmpty
    @Size(min = 1, max = 100)
    @UniqueUsername
    private String userName;

    @NotEmpty
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    @Size(min = 1, max = 50)
    @UniqueEmail
    private String userEmail;

    @NotNull
    private String userRole;

    public UserDto setUserName(String userName) {
        this.userName = userName.trim();
        return this;
    }

    public UserDto setUserEmail(String userEmail) {
        this.userEmail = userEmail.toLowerCase();
        return this;
    }

    public UserDto setUserRole(String userRole) {
        if (userRole == null || userRole.isBlank() || !existsByRole(userRole)) {
            this.userRole = Role.STUDENT.getRole();
        } else {
            this.userRole = userRole.trim().toLowerCase();
        }
        return this;
    }

    private boolean existsByRole(String userRole) {
        String temp = userRole.trim().toLowerCase();
        for (Role role : Role.values()) {
            if (temp.equals(role.getRole())) {
                return true;
            }
        }
        return false;
    }
}
