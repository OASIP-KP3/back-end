package sit.int221.oasipservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sit.int221.oasipservice.dto.api.EventDetailsBaseDto;
import sit.int221.oasipservice.dto.api.EventListAllDto;
import sit.int221.oasipservice.services.EventBookingService;

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
}
