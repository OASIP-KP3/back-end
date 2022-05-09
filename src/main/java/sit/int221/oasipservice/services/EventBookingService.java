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

import java.time.LocalDate;
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

        LocalDate date = booking.getEventStartTime().toLocalDate();
        Integer categoryId = booking.getEventCategory().getId();
        List<EventBooking> bookings = repo.findAllByDateAndCategory(date.toString(), categoryId);

        LocalTime startA = getStartTime(booking);
        LocalTime endA = getEndTime(booking);

        for (EventBooking book : bookings) {
            LocalTime startB = getStartTime(book);
            LocalTime endB = getEndTime(book);

            if (startA.isBefore(endB) && endA.isAfter(startB)) return true;
        }
        return false;
    }

    private LocalTime getStartTime(EventBooking booking) {
        return booking.getEventStartTime().toLocalTime();
    }

    private LocalTime getEndTime(EventBooking booking) {
        long minutes = booking.getEventDuration().longValue();
        return booking.getEventStartTime().toLocalTime().plusMinutes(minutes);
    }

    public void delete(Integer id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, id + " does not exist");
        }
    }
}
