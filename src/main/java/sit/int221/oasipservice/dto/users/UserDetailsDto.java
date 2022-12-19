package sit.int221.oasipservice.dto.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.oasipservice.entities.Role;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDetailsDto {
    private Integer id;
    private String userName;
    private String userEmail;
    @JsonIgnore
    private Set<Role> userRoles = new LinkedHashSet<>();

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private OffsetDateTime createdOn;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private OffsetDateTime updatedOn;

    // assume that user has only one role
    public String getRole() {
        return userRoles.stream().toList().get(0).getRoleName();
    }

    public String getCreatedDate() {
        return getLocalDate(createdOn);
    }

    public String getUpdatedDate() {
        return getLocalDate(updatedOn);
    }

    public String getCreatedTime() {
        return getLocalTime(createdOn);
    }

    public String getUpdatedTime() {
        return getLocalTime(updatedOn);
    }

    private String getLocalTime(OffsetDateTime dateTime) {
        return dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    private String getLocalDate(OffsetDateTime dateTime) {
        return dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }

    public OffsetDateTime getCreatedOn() {
        return createdOn.atZoneSameInstant(ZoneId.systemDefault()).toOffsetDateTime();
    }

    public OffsetDateTime getUpdatedOn() {
        return updatedOn.atZoneSameInstant(ZoneId.systemDefault()).toOffsetDateTime();
    }
}
