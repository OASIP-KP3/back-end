package sit.int221.oasipservice.dto.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDetailsDto {
    private Integer id;
    private String userName;
    private String userEmail;
    private String userRole;
    private OffsetDateTime createdOn;
    private OffsetDateTime updatedOn;

    public String getCreatedOn() {
        return createdOn.toString();
    }

    public String getUpdatedOn() {
        return updatedOn.toString();
    }
}
