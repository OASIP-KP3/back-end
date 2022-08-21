package sit.int221.oasipservice.dto.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
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
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    public OffsetDateTime getCreatedOn() {
        return OffsetDateTime.of(createdOn, ZoneOffset.ofHours(7));
    }

    public OffsetDateTime getUpdatedOn() {
        return OffsetDateTime.of(updatedOn, ZoneOffset.ofHours(7));
    }
}
