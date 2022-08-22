package sit.int221.oasipservice.dto.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDetailsDto {
    private Integer id;
    private String userName;
    private String userEmail;
    private String userRole;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private OffsetDateTime createdOn;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private OffsetDateTime updatedOn;

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
}
