package sit.int221.oasipservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sit.int221.oasipservice.dto.bookings.BookingDetailsDto;
import sit.int221.oasipservice.dto.bookings.BookingDto;
import sit.int221.oasipservice.dto.bookings.BookingViewDto;
import sit.int221.oasipservice.services.BookingService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/events")
public class BookingController {
    private final BookingService service;

    // default sorting by "eventStartTime" in descending
    @GetMapping("")
    public List<BookingViewDto> getEvents(
            @RequestParam(defaultValue = "eventStartTime") String sortBy,
            @RequestParam(defaultValue = "all") String type) {
        return service.getEvents(sortBy, type);
    }

    @GetMapping("/{id}")
    public BookingDetailsDto getEvent(@PathVariable Integer id) {
        return service.getEvent(id);
    }

    @PostMapping("")
    @ResponseStatus(CREATED)
//    @PreAuthorize("!isAuthenticated() or hasAnyAuthority('admin', 'student')")
    public void createEvent(@Valid @RequestBody BookingDto newBooking) {
        service.save(newBooking);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteEvent(@PathVariable Integer id) {
        service.delete(id);
    }

    @PatchMapping("/{id}")
    public BookingDetailsDto partialUpdateEvent(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body) {
        return service.update(id, body);
    }

    @GetMapping("/date/{date}")
    public List<BookingViewDto> getEventsByDate(@PathVariable String date) {
        return service.getEventsByDate(date);
    }
}
