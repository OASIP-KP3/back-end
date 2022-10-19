package sit.int221.oasipservice.dto.bookings;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class BookingViewDto {
    private Integer id;
    private String bookingName;
    @JsonIgnore
    private CategoryDto category;
    private LocalDateTime eventStartTime;
    private Integer eventDuration;

    public String getCategoryName() {
        return category.getCategoryName();
    }

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
