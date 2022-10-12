package sit.int221.oasipservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sit.int221.oasipservice.dto.events.EventBookingDto;
import sit.int221.oasipservice.dto.events.EventDetailsBaseDto;
import sit.int221.oasipservice.dto.events.EventListAllDto;
import sit.int221.oasipservice.dto.events.EventPartialUpdateDto;
import sit.int221.oasipservice.services.impl.BookingServiceImpl;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/events")
public class BookingControllerV2 {
    private final BookingServiceImpl service;

    // default sorting by "eventStartTime" in descending
    @GetMapping("")
    public List<EventListAllDto> getEvents(
            @RequestParam(defaultValue = "eventStartTime") String sortBy,
            @RequestParam(defaultValue = "all") String type) {
        return service.getEvents(sortBy, type);
    }

    @GetMapping("/{id}")
    public EventDetailsBaseDto getEvent(@PathVariable Integer id) {
        return service.getEvent(id);
    }

    @PostMapping("")
    @ResponseStatus(CREATED)
    public void createEvent(@Valid @RequestBody EventBookingDto newBooking) {
        service.save(newBooking);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Integer id) {
        service.delete(id);
    }

    @PatchMapping("/{id}")
    public EventPartialUpdateDto partialUpdateEvent(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        return service.update(id, body);
    }
}
