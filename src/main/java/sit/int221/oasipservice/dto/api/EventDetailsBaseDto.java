package sit.int221.oasipservice.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sit.int221.oasipservice.dto.EventCategoryDto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventDetailsBaseDto {
    @JsonIgnore
    private Integer id;
    private String bookingName;
    private String bookingEmail;
    @JsonIgnore
    private EventCategoryDto eventCategory;
    private Instant eventStartTime;
    private Integer eventDuration;
    private String eventNotes;

    public String getEventCategoryName() {
        return eventCategory.getCategoryName();
    }

    public String getEventStartDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        ZoneId zone = ZoneId.systemDefault();
        return LocalDate.ofInstant(eventStartTime, zone).format(formatter);
    }

    public String getEventStartTime() {
        ZoneId zone = ZoneId.systemDefault();
        return LocalTime.ofInstant(eventStartTime, zone).toString();
    }
}
