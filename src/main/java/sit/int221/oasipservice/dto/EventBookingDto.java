package sit.int221.oasipservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventBookingDto {
    private Integer id;
    private String bookingName;
    private String bookingEmail;
    private EventCategoryDto eventCategory;
    private Instant eventStartTime;
    private Integer eventDuration;
    private String eventNotes;
}
