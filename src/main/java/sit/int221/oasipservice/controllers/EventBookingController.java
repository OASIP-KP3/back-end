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

import java.util.List;

@CrossOrigin(origins = {"http://ip21kp3.sit.kmutt.ac.th", "http://localhost:3000", "http://intproj21.sit.kmutt.ac.th"})
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
    public void createEvent(@RequestBody EventBookingDto newBooking) {
        service.save(newBooking);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Integer id) {
        service.delete(id);
    }

    @PatchMapping("/datetime/{id}")
    public void updateDateTime(@PathVariable Integer id, @RequestBody EventDateTimeDto datetime) {
        service.updateDateTime(id, datetime);
    }

    @PatchMapping("/notes/{id}")
    public void updateNotes(@PathVariable Integer id, @RequestBody EventNotesDto notes) {
        service.updateNotes(id, notes);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The type of parameter is invalid")
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex) {
        return ex.getMessage();
    }
}
