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
import sit.int221.oasipservice.entities.EventBooking;
import sit.int221.oasipservice.repo.EventBookingRepository;
import sit.int221.oasipservice.utils.ListMapper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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

    public EventDetailsBaseDto getEventDetails(Integer id) {
        EventBooking booking = repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, id + " does not exist"));
        return modelMapper.map(booking, EventDetailsBaseDto.class);
    }

    public void save(EventBookingDto newBooking) {
        if (isOverlap(newBooking)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, newBooking.getEventStartTime() + " is overlap");
        } else {
            EventBooking booking = modelMapper.map(newBooking, EventBooking.class);
            repo.saveAndFlush(booking);
        }
    }

    private boolean isOverlap(EventBookingDto newBooking) {
        EventBooking booking = modelMapper.map(newBooking, EventBooking.class);

        ZoneId zone = ZoneId.systemDefault();
        LocalDate date = LocalDate.ofInstant(booking.getEventStartTime(), zone);
        Integer categoryId = booking.getEventCategory().getId();
        List<EventBooking> bookings = repo.findAllByDateAndCategory(date.toString(), categoryId);

        LocalTime startA = getStartTime(booking, zone);
        LocalTime endA = getEndTime(booking, zone);

        for (EventBooking book : bookings) {
            LocalTime startB = getStartTime(book, zone);
            LocalTime endB = getEndTime(book, zone);

            if (startA.isBefore(endB) && endA.isAfter(startB)) return true;
        }
        return false;
    }

    private Instant plusMinutes(Instant dateTime, long minutes) {
        return dateTime.plus(minutes, ChronoUnit.MINUTES);
    }

    private LocalTime getStartTime(EventBooking booking, ZoneId zone) {
        return LocalTime.ofInstant(booking.getEventStartTime(), zone);
    }

    private LocalTime getEndTime(EventBooking booking, ZoneId zone) {
        long minutes = booking.getEventDuration().longValue();
        return LocalTime.ofInstant(plusMinutes(booking.getEventStartTime(), minutes), zone);
    }

    public void delete(Integer id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, id + " does not exist");
        }
    }
}
