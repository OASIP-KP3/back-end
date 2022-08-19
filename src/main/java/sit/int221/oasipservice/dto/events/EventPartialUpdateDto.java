package sit.int221.oasipservice.dto.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventPartialUpdateDto {
    private Integer id;
    private LocalDateTime eventStartTime;
    private String eventNotes;

    public EventPartialUpdateDto setEventNotes(String eventNotes) {
        this.eventNotes = eventNotes == null || eventNotes.isBlank() ? null : eventNotes.trim();
        return this;
    }

    public String getEventNewDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return eventStartTime.toLocalDate().format(formatter);
    }

    public String getEventNewTime() {
        return eventStartTime.toLocalTime().toString();
    }

    public String getNewDateTime() {
        return eventStartTime.toString();
    }
}
