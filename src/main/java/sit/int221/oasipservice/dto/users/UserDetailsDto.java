package sit.int221.oasipservice.dto.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

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

    public OffsetDateTime getCreatedOn() {
        return createdOn.withOffsetSameLocal(ZoneOffset.of(ZoneId.systemDefault().getId()));
    }

    public OffsetDateTime getUpdatedOn() {
        return updatedOn.withOffsetSameLocal(ZoneOffset.of(ZoneId.systemDefault().getId()));
    }
}
