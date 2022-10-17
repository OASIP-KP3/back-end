package sit.int221.oasipservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.oasipservice.dto.bookings.BookingDto;
import sit.int221.oasipservice.dto.bookings.BookingDetailsDto;
import sit.int221.oasipservice.dto.bookings.BookingViewDto;
import sit.int221.oasipservice.dto.bookings.fields.EventDateTimeDto;
import sit.int221.oasipservice.dto.bookings.fields.EventNotesDto;
import sit.int221.oasipservice.entities.EventBooking;
import sit.int221.oasipservice.repositories.BookingRepository;
import sit.int221.oasipservice.utils.ListMapper;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository repo;
    private final ModelMapper modelMapper;
    private final ListMapper listMapper;

    @Autowired
    public BookingService(BookingRepository repo, ModelMapper modelMapper, ListMapper listMapper) {
        this.repo = repo;
        this.modelMapper = modelMapper;
        this.listMapper = listMapper;
    }

    public List<BookingViewDto> getEventListSorted() {
        Sort sort = Sort.by("eventStartTime");
        List<EventBooking> bookings = repo.findAll(sort.descending());
        return listMapper.mapList(bookings, BookingViewDto.class, modelMapper);
    }

    public BookingDetailsDto getEventDetails(Integer id) throws ResourceNotFoundException {
        EventBooking booking = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        return modelMapper.map(booking, BookingDetailsDto.class);
    }

    public void save(BookingDto newBooking) throws ResponseStatusException {
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
            if (isOverlap(id, categoryId, newBooking.getEventStartTime(), duration)) {
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

    public List<BookingViewDto> getEventsBy(String type) throws ResourceNotFoundException {
        return switch (type) {
            case "future" -> getFutureEvents();
            case "past" -> getPastEvents();
            case "all" -> getEventListSorted();
            default -> throw new ResourceNotFoundException("Type " + type + " is not supported");
        };
    }

    private List<BookingViewDto> getFutureEvents() {
        List<EventBooking> bookings = repo.getFutureEvents();
        return listMapper.mapList(bookings, BookingViewDto.class, modelMapper);
    }

    private List<BookingViewDto> getPastEvents() {
        List<EventBooking> bookings = repo.getPastEvents();
        return listMapper.mapList(bookings, BookingViewDto.class, modelMapper);
    }

    public List<BookingViewDto> getEventsByDate(String date) {
        List<EventBooking> bookings = repo.findAllByByDate(date);
        return listMapper.mapList(bookings, BookingViewDto.class, modelMapper);
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
