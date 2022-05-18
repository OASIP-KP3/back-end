package sit.int221.oasipservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.oasipservice.dto.events.EventBookingDto;
import sit.int221.oasipservice.dto.events.EventDetailsBaseDto;
import sit.int221.oasipservice.dto.events.EventListAllDto;
import sit.int221.oasipservice.dto.events.fields.EventDateTimeDto;
import sit.int221.oasipservice.dto.events.fields.EventNotesDto;
import sit.int221.oasipservice.entities.EventBooking;
import sit.int221.oasipservice.repo.EventBookingRepository;
import sit.int221.oasipservice.utils.ListMapper;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class EventBookingService {
    private final EventBookingRepository repo;
    private final ModelMapper modelMapper;
    private final ListMapper listMapper;

    @Autowired
    public EventBookingService(EventBookingRepository repo, ModelMapper modelMapper, ListMapper listMapper) {
        this.repo = repo;
        this.modelMapper = modelMapper;
        this.listMapper = listMapper;
    }

    public List<EventListAllDto> getEventListSorted() {
        Sort sort = Sort.by("eventStartTime");
        List<EventBooking> bookings = repo.findAll(sort.descending());
        return listMapper.mapList(bookings, EventListAllDto.class, modelMapper);
    }

    public EventDetailsBaseDto getEventDetails(Integer id) throws ResourceNotFoundException {
        EventBooking booking = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        return modelMapper.map(booking, EventDetailsBaseDto.class);
    }

    public void save(EventBookingDto newBooking) throws ResponseStatusException {
        if (isOverlap(newBooking.getCategoryId(), newBooking.getEventStartTime(), newBooking.getEventDuration())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, newBooking.getEventStartTime() + " is overlap");
        } else {
            EventBooking booking = modelMapper.map(newBooking, EventBooking.class);
            repo.saveAndFlush(booking);
        }
    }

    public EventDateTimeDto updateDateTime(Integer id, EventDateTimeDto booking) throws ResourceNotFoundException, ResponseStatusException {
        EventBooking newBooking = modelMapper.map(booking, EventBooking.class);
        EventBooking updatedBooking = repo.findById(id).map(oldBooking -> {
            Integer duration = repo.getEventDurationById(id);
            Integer categoryId = repo.getEventCategoryIdById(id);
            if (oldBooking.getEventStartTime().equals(newBooking.getEventStartTime())) {
                return oldBooking;
            } else if (isOverlap(categoryId, newBooking.getEventStartTime(), duration)) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, newBooking.getEventStartTime() + " is overlap");
            } else {
                oldBooking.setEventStartTime(newBooking.getEventStartTime());
                return oldBooking;
            }
        }).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        return modelMapper.map(repo.saveAndFlush(updatedBooking), EventDateTimeDto.class);
    }

    public EventNotesDto updateNotes(Integer id, EventNotesDto notes) throws ResourceNotFoundException {
        EventBooking newBooking = modelMapper.map(notes, EventBooking.class);
        EventBooking updatedBooking = repo.findById(id).map(oldBooking -> {
            oldBooking.setEventNotes(newBooking.getEventNotes());
            return oldBooking;
        }).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        return modelMapper.map(repo.saveAndFlush(updatedBooking), EventNotesDto.class);
    }

    public void delete(Integer id) throws ResourceNotFoundException {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("ID " + id + " is not found");
        }
        repo.deleteById(id);
    }

    private boolean isOverlap(Integer categoryId, LocalDateTime dateTime, Integer duration) {
        LocalTime startA = getStartTime(dateTime);
        LocalTime endA = getEndTime(dateTime, duration);
        String date = dateTime.toLocalDate().toString();
        List<EventBooking> bookings = repo.findAllByDateAndCategory(date, categoryId);

        if (bookings.isEmpty()) return false;
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
