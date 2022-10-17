package sit.int221.oasipservice.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.oasipservice.dto.bookings.BookingDetailsDto;
import sit.int221.oasipservice.dto.bookings.BookingDto;
import sit.int221.oasipservice.dto.bookings.BookingViewDto;
import sit.int221.oasipservice.entities.EventBooking;
import sit.int221.oasipservice.repositories.BookingRepository;
import sit.int221.oasipservice.services.BookingService;
import sit.int221.oasipservice.utils.ListMapper;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Service
@Log4j2
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repo;
    private final ModelMapper modelMapper;
    private final ListMapper listMapper;

    @Override
    public List<BookingViewDto> getEvents(String sortBy, String type) throws IllegalArgumentException {
        log.info("Fetching all bookings...");
        List<EventBooking> bookings = repo.findAll(Sort.by(sortBy).descending());
        return switch (type) {
            case "all" -> listMapper.mapList(bookings, BookingViewDto.class, modelMapper);
            case "end" -> listMapper.mapList(repo.getPastEvents(), BookingViewDto.class, modelMapper);
            case "ongoing" -> listMapper.mapList(repo.getFutureEvents(), BookingViewDto.class, modelMapper);
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }

    @Override
    public BookingDetailsDto getEvent(Integer id) throws ResourceNotFoundException {
        log.info("Fetching booking id: " + id);
        EventBooking booking = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        return modelMapper.map(booking, BookingDetailsDto.class);
    }

    @Override
    public void save(BookingDto newBooking) throws ResponseStatusException {
        log.info("Saving a new booking...");
        if (isOverlap(newBooking.getCategoryId(), newBooking.getEventStartTime(), newBooking.getEventDuration()))
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, newBooking.getEventStartTime() + " is overlap");
        repo.saveAndFlush(modelMapper.map(newBooking, EventBooking.class));
    }

    @Override
    public void delete(Integer id) throws ResourceNotFoundException {
        log.info("Deleting booking id: " + id);
        if (!repo.existsById(id)) throw new ResourceNotFoundException("ID " + id + " is not found");
        repo.deleteById(id);
    }

    @Override
    public BookingDetailsDto update(Integer id, Map<String, Object> changes) throws ResourceNotFoundException, ResponseStatusException, IllegalArgumentException {
        EventBooking booking = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        changes.forEach((field, value) -> {
            switch (field) {
                case "eventStartTime" -> {
                    if (value == null) throw new IllegalArgumentException(field + " is must not be null");
                    final String TIME_ZONE = "GMT+07:00";
                    LocalDateTime dateTime = LocalDateTime.parse((String) value);
                    if (dateTime.isBefore(LocalDateTime.now(ZoneId.of(TIME_ZONE))))
                        throw new IllegalArgumentException(field + " is must be a date and time in the future");
                    Integer duration = repo.getEventDurationById(id);
                    Integer categoryId = repo.getEventCategoryIdById(id);
                    if (isOverlap(id, categoryId, dateTime, duration))
                        throw new ResponseStatusException(UNPROCESSABLE_ENTITY, dateTime + " is overlap");
                    log.info("Updating start time of id: " + id);
                    booking.setEventStartTime(LocalDateTime.parse((String) value));
                }
                case "eventNotes" -> {
                    String notes = (String) value;
                    if (notes.length() > 500) throw new IllegalArgumentException("size must be between 0 and 500");
                    log.info("Updating notes of id: " + id);
                    booking.setEventNotes(notes);
                }
                default -> throw new IllegalArgumentException("Unknown field: " + field);
            }
        });
        return modelMapper.map(repo.saveAndFlush(booking), BookingDetailsDto.class);
    }

    @Override
    public List<BookingViewDto> getEventsByDate(String date) {
        log.info("Fetching all bookings by date: " + date);
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
