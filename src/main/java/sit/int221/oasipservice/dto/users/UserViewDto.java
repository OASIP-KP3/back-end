package sit.int221.oasipservice.dto.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.oasipservice.entities.Role;

import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserViewDto {
    private Integer id;
    private String userName;
    private String userEmail;
    @JsonIgnore
    private Set<Role> userRoles = new LinkedHashSet<>();

    // assume that user has only one role
    public String getRole() {
        return userRoles.stream().toList().get(0).getRoleName();
    }
}
