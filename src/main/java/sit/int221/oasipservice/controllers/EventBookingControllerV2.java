package sit.int221.oasipservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sit.int221.oasipservice.dto.events.EventBookingDto;
import sit.int221.oasipservice.dto.events.EventDetailsBaseDto;
import sit.int221.oasipservice.dto.events.EventListPageDto;
import sit.int221.oasipservice.dto.events.EventPartialUpdateDto;
import sit.int221.oasipservice.services.EventBookingServiceV2;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/events")
public class EventBookingControllerV2 {
    private final EventBookingServiceV2 service;

    @Autowired
    public EventBookingControllerV2(EventBookingServiceV2 service) {
        this.service = service;
    }

    // default sorting by "eventStartTime" in descending
    @GetMapping("")
    public EventListPageDto getALlEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "eventStartTime") String sortBy) {
        return service.getEvents(page, pageSize, sortBy);
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

    @PatchMapping("/{id}")
    public EventPartialUpdateDto partialUpdateEvent(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        return service.update(id, body);
    }
}
