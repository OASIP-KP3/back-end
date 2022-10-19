package sit.int221.oasipservice.dto.bookings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.oasipservice.dto.categories.CategoryDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingDetailsDto {
    private Integer id;
    private String bookingName;
    private String bookingEmail;
    private CategoryDto category;
    private LocalDateTime eventStartTime;
    private String eventNotes;

    public String getEventStartDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return eventStartTime.toLocalDate().format(formatter);
    }

    public String getEventStartTime() {
        return eventStartTime.toLocalTime().toString();
    }

    public String getStartDateTime() {
        return eventStartTime.toString();
    }
}
