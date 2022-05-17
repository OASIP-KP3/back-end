package sit.int221.oasipservice.dto.events.fields;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.FutureOrPresent;
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
    @FutureOrPresent
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
