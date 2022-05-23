package sit.int221.oasipservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sit.int221.oasipservice.dto.events.EventBookingDto;
import sit.int221.oasipservice.dto.events.EventDetailsBaseDto;
import sit.int221.oasipservice.dto.events.EventListAllDto;
import sit.int221.oasipservice.dto.events.fields.EventDateTimeDto;
import sit.int221.oasipservice.dto.events.fields.EventNotesDto;
import sit.int221.oasipservice.services.EventBookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventBookingController {
    private final EventBookingService service;

    @Autowired
    public EventBookingController(EventBookingService service) {
        this.service = service;
    }

    @GetMapping("")
    public List<EventListAllDto> getEventListSorted() {
        return service.getEventListSorted();
    }

    @GetMapping("/{id}")
    public EventDetailsBaseDto getEventDetails(@PathVariable Integer id) {
        return service.getEventDetails(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void createEvent(@Valid @RequestBody EventBookingDto newBooking) {
        service.save(newBooking);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Integer id) {
        service.delete(id);
    }

    @PatchMapping("/{id}/datetime")
    public EventDateTimeDto updateDateTime(@PathVariable Integer id, @Valid @RequestBody EventDateTimeDto dateTime) {
        return service.updateDateTime(id, dateTime);
    }

    @PatchMapping("/{id}/notes")
    public EventNotesDto updateNotes(@PathVariable Integer id, @Valid @RequestBody EventNotesDto notes) {
        return service.updateNotes(id, notes);
    }

    @GetMapping("/types")
    public List<EventListAllDto> getEventsBy(@RequestParam(defaultValue = "future") String type) {
        return service.getEventsBy(type);
    }

    @GetMapping("/dates/{date}")
    public List<EventListAllDto> getEventsByDate(@PathVariable String date) {
        return service.getEventsByDate(date);
    }
}
