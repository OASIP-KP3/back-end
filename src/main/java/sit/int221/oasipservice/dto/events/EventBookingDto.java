package sit.int221.oasipservice.dto.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.ZoneId;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventBookingDto {
    private Integer id;
    private String bookingName;
    private String bookingEmail;
    private Integer categoryId;
    private Instant eventStartTime;
    private Integer eventDuration;
    private String eventNotes;

    public EventBookingDto setEventStartTime(Instant eventStartTime) {
        this.eventStartTime = eventStartTime.atZone(ZoneId.systemDefault()).toInstant();
        return this;
    }
}
