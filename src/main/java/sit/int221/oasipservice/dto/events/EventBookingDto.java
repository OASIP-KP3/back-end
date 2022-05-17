package sit.int221.oasipservice.dto.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventBookingDto {
    private Integer id;

    @NotEmpty
    @Size(min = 1, max = 100)
    private String bookingName;

    @NotEmpty
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    @Size(min = 1, max = 64)
    private String bookingEmail;

    @NotNull
    private Integer categoryId;

    @NotNull
    @FutureOrPresent
    private LocalDateTime eventStartTime;

    @NotNull
    @Min(1)
    @Max(480)
    private Integer eventDuration;

    @Size(max = 500)
    private String eventNotes;

    public EventBookingDto setBookingName(String bookingName) {
        this.bookingName = bookingName.trim();
        return this;
    }

    public EventBookingDto setBookingEmail(String bookingEmail) {
        this.bookingEmail = bookingEmail.trim();
        return this;
    }

    public EventBookingDto setEventNotes(String eventNotes) {
        this.eventNotes = eventNotes == null || eventNotes.isBlank() ? null : eventNotes.trim();
        return this;
    }
}
