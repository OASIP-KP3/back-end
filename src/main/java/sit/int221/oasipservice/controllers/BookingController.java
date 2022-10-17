package sit.int221.oasipservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sit.int221.oasipservice.dto.bookings.BookingDto;
import sit.int221.oasipservice.dto.bookings.BookingDetailsDto;
import sit.int221.oasipservice.dto.bookings.BookingViewDto;
import sit.int221.oasipservice.dto.bookings.fields.EventDateTimeDto;
import sit.int221.oasipservice.dto.bookings.fields.EventNotesDto;
import sit.int221.oasipservice.services.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class BookingController {
    private final BookingService service;

    @Autowired
    public BookingController(BookingService service) {
        this.service = service;
    }

    @GetMapping("")
    public List<BookingViewDto> getEventListSorted() {
        return service.getEventListSorted();
    }

    @GetMapping("/{id}")
    public BookingDetailsDto getEventDetails(@PathVariable Integer id) {
        return service.getEventDetails(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void createEvent(@Valid @RequestBody BookingDto newBooking) {
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
    public List<BookingViewDto> getEventsBy(@RequestParam(defaultValue = "future") String type) {
        return service.getEventsBy(type);
    }

    @GetMapping("/dates/{date}")
    public List<BookingViewDto> getEventsByDate(@PathVariable String date) {
        return service.getEventsByDate(date);
    }
}
