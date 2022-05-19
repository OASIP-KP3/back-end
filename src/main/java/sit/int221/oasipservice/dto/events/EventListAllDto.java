package sit.int221.oasipservice.dto.events;

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
public class EventListAllDto {
    private Integer id;
    private String bookingName;
    @JsonIgnore
    private CategoryDto eventCategory;
    private LocalDateTime eventStartTime;
    private Integer eventDuration;

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
