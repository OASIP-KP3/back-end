package sit.int221.oasipservice.dto.events.fields;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventNotesDto {
    private Integer id;

    @Size(max = 500)
    private String eventNotes;

    public EventNotesDto setEventNotes(String eventNotes) {
        this.eventNotes = eventNotes == null || eventNotes.isBlank() ? null : eventNotes.trim();
        return this;
    }
}
