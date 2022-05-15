package sit.int221.oasipservice.dto.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.oasipservice.dto.categories.EventCategoryDto;

import java.time.*;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventDetailsBaseDto {
    private Integer id;
    private String bookingName;
    private String bookingEmail;
    @JsonIgnore
    private EventCategoryDto eventCategory;
    private LocalDateTime eventStartTime;
    private Integer eventDuration;
    private String eventNotes;

    public String getEventCategoryName() {
        return eventCategory.getCategoryName();
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
