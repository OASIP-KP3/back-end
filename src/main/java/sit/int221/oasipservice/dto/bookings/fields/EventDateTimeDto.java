package sit.int221.oasipservice.dto.bookings.fields;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.oasipservice.annotations.FutureValidation;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventDateTimeDto {
    private Integer id;

    @NotNull
    @FutureValidation
    private LocalDateTime eventStartTime;

    public String getEventNewDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return eventStartTime.toLocalDate().format(formatter);
    }

    public String getEventNewTime() {
        return eventStartTime.toLocalTime().toString();
    }

    public String getNewDateTime() {
        return eventStartTime.toString();
    }
}
