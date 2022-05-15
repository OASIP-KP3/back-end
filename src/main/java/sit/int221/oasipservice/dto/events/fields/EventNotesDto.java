package sit.int221.oasipservice.dto.events.fields;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventNotesDto {
    private Integer id;
    private String eventNotes;

    public EventNotesDto setEventNotes(String eventNotes) {
        this.eventNotes = eventNotes == null || eventNotes.isBlank() ? null : eventNotes.trim();
        return this;
    }
}
