package sit.int221.oasipservice.dto.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.*;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventBookingDto {
    private Integer id;
    private String bookingName;
    private String bookingEmail;
    private Integer categoryId;
    private LocalDateTime eventStartTime;
    private Integer eventDuration;
    private String eventNotes;
}
