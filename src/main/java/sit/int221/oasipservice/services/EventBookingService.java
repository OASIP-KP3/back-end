package sit.int221.oasipservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
import java.util.regex.Pattern;

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

    public EventDetailsBaseDto getEventDetails(Integer id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, id + " does not exist");
        } else {
            EventBooking booking = repo.getById(id);
            return modelMapper.map(booking, EventDetailsBaseDto.class);
        }
    }

    public void save(EventBookingDto newBooking) {
        if (isOverlap(newBooking.getCategoryId(), newBooking.getEventStartTime(), newBooking.getEventDuration())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, newBooking.getEventStartTime() + " is overlap");
        }
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        if (!isValidEmail(newBooking.getBookingEmail(), regexPattern)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, newBooking.getBookingEmail() + " is a wrong format");
        } else {
            EventBooking booking = modelMapper.map(newBooking, EventBooking.class);
            repo.saveAndFlush(booking);
        }
    }

    private boolean isValidEmail(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern).matcher(emailAddress).matches();
    }

    public void updateDateTime(Integer id, EventDateTimeDto booking) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, id + " does not exist");
        } else {
            EventBooking newBooking = modelMapper.map(booking, EventBooking.class);
            EventBooking updatedBooking = repo.findById(id).map(oldBooking -> {
                Integer duration = repo.getEventDurationById(id);
                Integer categoryId = repo.getEventCategoryIdById(id);
                if (oldBooking.getEventStartTime().equals(newBooking.getEventStartTime())) {
                    return oldBooking;
                } else if (isOverlap(categoryId, newBooking.getEventStartTime(), duration)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, newBooking.getEventStartTime() + " is overlap");
                } else {
                    oldBooking.setEventStartTime(newBooking.getEventStartTime());
                    return oldBooking;
                }
            }).orElseThrow();
            repo.saveAndFlush(updatedBooking);
        }
    }

    public void updateNotes(Integer id, EventNotesDto notes) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, id + " does not exist");
        } else {
            EventBooking newBooking = modelMapper.map(notes, EventBooking.class);
            EventBooking updatedBooking = repo.findById(id).map(oldBooking -> {
                oldBooking.setEventNotes(newBooking.getEventNotes());
                return oldBooking;
            }).orElseThrow();
            repo.saveAndFlush(updatedBooking);
        }
    }

    private boolean isOverlap(Integer categoryId, LocalDateTime dateTime, Integer duration) {
        LocalTime startA = getStartTime(dateTime);
        LocalTime endA = getEndTime(dateTime, duration);
        String date = dateTime.toLocalDate().toString();
        List<EventBooking> bookings = repo.findAllByDateAndCategory(date, categoryId, startA.getHour());

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

    public void delete(Integer id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, id + " does not exist");
        } else {
            repo.deleteById(id);
        }
    }
}
