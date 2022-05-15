package sit.int221.oasipservice.dto.events.fields;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventDateTimeDto {
    private Integer id;
    private LocalDateTime eventStartTime;
}
