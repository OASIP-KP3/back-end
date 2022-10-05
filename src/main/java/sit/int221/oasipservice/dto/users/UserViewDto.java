package sit.int221.oasipservice.dto.users;

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
    private Set<Role> userRoles = new LinkedHashSet<>();
}
