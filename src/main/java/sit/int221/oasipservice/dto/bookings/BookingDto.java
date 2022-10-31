package sit.int221.oasipservice.dto.bookings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.oasipservice.annotations.FutureValidation;
import sit.int221.oasipservice.annotations.UniqueCategoryId;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingDto {
    private Integer id;

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 100)
    @Pattern(regexp = "^[^\s].+[^\s]$", message = "leading or trailing white spaces do not allowed in the string")
    private String bookingName;

    @NotNull
    @NotEmpty
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    @Size(min = 1, max = 64)
    private String bookingEmail;

    @NotNull
    @Min(1)
    @UniqueCategoryId
    private Integer categoryId;

    @NotNull
    @FutureValidation
    private LocalDateTime eventStartTime;

    @NotNull
    @Min(1)
    @Max(480)
    private Integer eventDuration;

    @Size(max = 500)
    private String eventNotes;

    public BookingDto setBookingEmail(String bookingEmail) {
        this.bookingEmail = bookingEmail.toLowerCase();
        return this;
    }

    public BookingDto setEventNotes(String eventNotes) {
        this.eventNotes = eventNotes == null || eventNotes.isBlank() ? null : eventNotes.trim();
        return this;
    }
}
