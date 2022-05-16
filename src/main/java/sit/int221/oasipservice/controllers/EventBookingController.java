package sit.int221.oasipservice.controllers;

import org.hibernate.QueryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import sit.int221.oasipservice.dto.events.EventBookingDto;
import sit.int221.oasipservice.dto.events.EventDetailsBaseDto;
import sit.int221.oasipservice.dto.events.EventListAllDto;
import sit.int221.oasipservice.dto.events.fields.EventDateTimeDto;
import sit.int221.oasipservice.dto.events.fields.EventNotesDto;
import sit.int221.oasipservice.services.EventBookingService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;

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
        EventDetailsBaseDto details;
        try {
            details = service.getEventDetails(id);
        } catch (QueryException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        return details;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void createEvent(@RequestBody EventBookingDto newBooking) {
        try {
            service.save(newBooking);
        } catch (IllegalArgumentException ex) {
            throw new ServerWebInputException(ex.getMessage());
        } catch (InputMismatchException ex) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Integer id) {
        try {
            service.delete(id);
        } catch (QueryException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PatchMapping("/datetime/{id}")
    public void updateDateTime(@PathVariable Integer id, @RequestBody EventDateTimeDto datetime) {
        try {
            service.updateDateTime(id, datetime);
        } catch (QueryException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (IllegalArgumentException ex) {
            throw new ServerWebInputException(ex.getMessage());
        }
    }

    @PatchMapping("/notes/{id}")
    public void updateNotes(@PathVariable Integer id, @RequestBody EventNotesDto notes) {
        try {
            service.updateNotes(id, notes);
        } catch (QueryException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The type of parameter is invalid")
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ex.getMessage();
    }
}
