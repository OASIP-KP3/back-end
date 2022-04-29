package sit.int221.oasipservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventCategoryDto {
    private Integer id;
    private String categoryName;
    private String categoryDescription;
    private Integer eventDuration;
    private Set<EventBookingDto> eventBookings = new LinkedHashSet<>();
}
