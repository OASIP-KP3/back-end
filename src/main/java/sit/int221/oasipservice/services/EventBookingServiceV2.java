package sit.int221.oasipservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.oasipservice.dto.events.EventBookingDto;
import sit.int221.oasipservice.dto.events.EventDetailsBaseDto;
import sit.int221.oasipservice.dto.events.EventListPageDto;
import sit.int221.oasipservice.dto.events.EventPartialUpdateDto;
import sit.int221.oasipservice.entities.EventBooking;
import sit.int221.oasipservice.repo.EventBookingRepository;
import sit.int221.oasipservice.utils.ListMapper;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Service
public class EventBookingServiceV2 {
    private final EventBookingRepository repo;
    private final ModelMapper modelMapper;
    private final ListMapper listMapper;

    @Autowired
    public EventBookingServiceV2(EventBookingRepository repo, ModelMapper modelMapper, ListMapper listMapper) {
        this.repo = repo;
        this.modelMapper = modelMapper;
        this.listMapper = listMapper;
    }

    public EventListPageDto getEvents(int page, int pageSize, String sortBy) {
        Sort sort = Sort.by(sortBy).descending();
        return modelMapper.map(repo.findAll(PageRequest.of(page, pageSize, sort)), EventListPageDto.class);
    }

    public EventDetailsBaseDto getEventDetails(Integer id) throws ResourceNotFoundException {
        EventBooking booking = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        return modelMapper.map(booking, EventDetailsBaseDto.class);
    }

    public void save(EventBookingDto newBooking) throws ResponseStatusException {
        if (isOverlap(newBooking.getCategoryId(), newBooking.getEventStartTime(), newBooking.getEventDuration())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, newBooking.getEventStartTime() + " is overlap");
        }
        repo.saveAndFlush(modelMapper.map(newBooking, EventBooking.class));
    }

    public void delete(Integer id) throws ResourceNotFoundException {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("ID " + id + " is not found");
        }
        repo.deleteById(id);
    }

    public EventPartialUpdateDto update(Integer id, Map<String, Object> changes) throws ResourceNotFoundException, ResponseStatusException, IllegalArgumentException {
        EventBooking booking = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        changes.forEach((field, value) -> {
            switch (field) {
                case "eventStartTime" -> {
                    if (value == null) {
                        throw new IllegalArgumentException(field + " is must not be null");
                    }
                    final String TIME_ZONE = "GMT+07:00";
                    LocalDateTime dateTime = LocalDateTime.parse((String) value);
                    if (dateTime.isBefore(LocalDateTime.now(ZoneId.of(TIME_ZONE)))) {
                        throw new IllegalArgumentException(field + " is must be a date and time in the future");
                    }
                    Integer duration = repo.getEventDurationById(id);
                    Integer categoryId = repo.getEventCategoryIdById(id);
                    if (isOverlap(id, categoryId, dateTime, duration)) {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, dateTime + " is overlap");
                    }
                    booking.setEventStartTime(LocalDateTime.parse((String) value));
                }
                case "eventNotes" -> {
                    String notes = (String) value;
                    if (notes.length() > 500) {
                        throw new IllegalArgumentException("size must be between 0 and 500");
                    }
                    booking.setEventNotes(notes);
                }
                default -> throw new IllegalArgumentException("Unknown field: " + field);
            }
        });
        return modelMapper.map(repo.saveAndFlush(booking), EventPartialUpdateDto.class);
    }

    private boolean isOverlap(Integer id, Integer categoryId, LocalDateTime dateTime, Integer duration) {
        LocalTime startA = getStartTime(dateTime);
        LocalTime endA = getEndTime(dateTime, duration);
        String date = dateTime.toLocalDate().toString();
        List<EventBooking> bookings = repo.findAllByDateAndCategory(date, categoryId, id);
        return checkOverlap(startA, endA, bookings);
    }


    private boolean isOverlap(Integer categoryId, LocalDateTime dateTime, Integer duration) {
        LocalTime startA = getStartTime(dateTime);
        LocalTime endA = getEndTime(dateTime, duration);
        String date = dateTime.toLocalDate().toString();
        List<EventBooking> bookings = repo.findAllByDateAndCategory(date, categoryId);
        return checkOverlap(startA, endA, bookings);
    }

    private boolean checkOverlap(LocalTime startA, LocalTime endA, List<EventBooking> bookings) {
        if (bookings.isEmpty()) {
            return false;
        }
        for (EventBooking booking : bookings) {
            LocalTime startB = getStartTime(booking.getEventStartTime());
            LocalTime endB = getEndTime(booking.getEventStartTime(), booking.getEventDuration());
            if (startA.isBefore(endB) && endA.isAfter(startB)) return true;
        }
        return false;
    }

    private LocalTime getStartTime(LocalDateTime dateTime) {
        return dateTime.toLocalTime();
    }

    private LocalTime getEndTime(LocalDateTime dateTime, Integer duration) {
        return dateTime.toLocalTime().plusMinutes(duration.longValue());
    }
}
