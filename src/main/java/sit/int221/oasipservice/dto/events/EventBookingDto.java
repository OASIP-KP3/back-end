package sit.int221.oasipservice.dto.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.regex.Pattern;

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

    public EventBookingDto setBookingEmail(String bookingEmail) {
        this.bookingEmail = bookingEmail.trim();
        return this;
    }

    public EventBookingDto setBookingName(String bookingName) {
        this.bookingName = bookingName.trim();
        return this;
    }

    public EventBookingDto setEventNotes(String eventNotes) {
        this.eventNotes = eventNotes.isBlank() ? null : eventNotes.trim();
        return this;
    }
}
