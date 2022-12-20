package sit.int221.oasipservice.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import sit.int221.oasipservice.dto.bookings.BookingDetailsDto;
import sit.int221.oasipservice.dto.bookings.BookingDto;
import sit.int221.oasipservice.dto.bookings.BookingViewDto;
import sit.int221.oasipservice.entities.EventBooking;
import sit.int221.oasipservice.entities.EventCategory;
import sit.int221.oasipservice.entities.User;
import sit.int221.oasipservice.exceptions.ForbiddenException;
import sit.int221.oasipservice.exceptions.UnprocessableException;
import sit.int221.oasipservice.repositories.BookingRepository;
import sit.int221.oasipservice.repositories.CategoryRepository;
import sit.int221.oasipservice.repositories.UserRepository;
import sit.int221.oasipservice.services.BookingService;
import sit.int221.oasipservice.utils.JwtUtils;
import sit.int221.oasipservice.utils.ListMapper;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sit.int221.oasipservice.entities.ERole.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepo;
    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;
    private final ModelMapper modelMapper;
    private final ListMapper listMapper;
    private final JwtUtils jwtUtils;

    @Override
    public List<BookingViewDto> getEvents(String sortBy, String type) {
        if (jwtUtils.getRoles().contains(ROLE_STUDENT.getRole())) {
            log.info("[" + ROLE_STUDENT.getRole() + "]" + " Fetching all bookings...");
            String email = jwtUtils.getEmail();
            return switch (type) {
                case "all" ->
                        listMapper.mapList(bookingRepo.findByBookingEmail(email), BookingViewDto.class, modelMapper);
                case "end" -> listMapper.mapList(bookingRepo.getPastEvents(email), BookingViewDto.class, modelMapper);
                case "ongoing" ->
                        listMapper.mapList(bookingRepo.getFutureEvents(email), BookingViewDto.class, modelMapper);
                default -> throw new IllegalArgumentException("Unknown type: " + type);
            };
        }
        if (jwtUtils.getRoles().contains(ROLE_LECTURER.getRole())) {
            log.info("[" + ROLE_LECTURER.getRole() + "]" + " Fetching all bookings...");
            User lecturer = userRepo.findByUserEmail(jwtUtils.getEmail());
            List<BookingViewDto> bookings = new ArrayList<>();
            lecturer.getOwnCategories()
                    .stream()
                    .map(EventCategory::getEventBookings)
                    .map(b -> listMapper.mapList(b, BookingViewDto.class, modelMapper))
                    .forEach(bookings::addAll);
            if (!type.equals("all")) {
                List<BookingViewDto> filtered = new ArrayList<>(
                        bookings
                                .stream()
                                .filter(b -> {
                                    final String TIME_ZONE = "GMT+07:00";
                                    LocalDateTime endDateTime = getEndDateTime(b);
                                    switch (type) {
                                        case "end" -> {
                                            return endDateTime.isBefore(LocalDateTime.now(ZoneId.of(TIME_ZONE)));
                                        }
                                        case "ongoing" -> {
                                            return endDateTime.isAfter(LocalDateTime.now(ZoneId.of(TIME_ZONE)));
                                        }
                                        default -> throw new IllegalArgumentException("Unknown type: " + type);
                                    }
                                }).toList()
                );
                switch (type) {
                    case "end" -> filtered.sort((o1, o2) -> getEndDateTime(o2).compareTo(getEndDateTime(o1)));
                    case "ongoing" -> filtered.sort((o1, o2) -> getEndDateTime(o1).compareTo(getEndDateTime(o2)));
                    default -> throw new IllegalArgumentException("Unknown type: " + type);
                }
                return filtered;
            }
            bookings.sort(((o1, o2) -> getEndDateTime(o2).compareTo(getEndDateTime(o1))));
            return bookings;
        }
        if (jwtUtils.getRoles().contains(ROLE_ADMIN.getRole())) {
            log.info("[" + ROLE_ADMIN.getRole() + "]" + " Fetching all bookings...");
        } else {
            log.info("Fetching all bookings...");
        }
        List<EventBooking> bookings = bookingRepo.findAll(Sort.by(sortBy).descending());
        return switch (type) {
            case "all" -> listMapper.mapList(bookings, BookingViewDto.class, modelMapper);
            case "end" -> listMapper.mapList(bookingRepo.getPastEvents(), BookingViewDto.class, modelMapper);
            case "ongoing" -> listMapper.mapList(bookingRepo.getFutureEvents(), BookingViewDto.class, modelMapper);
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }

    @Override
    public BookingDetailsDto getEvent(Integer id) {
        EventBooking booking = bookingRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("booking id " + id + " is not found"));
        if (jwtUtils.getRoles().contains(ROLE_STUDENT.getRole())) {
            if (!jwtUtils.getEmail().equals(booking.getBookingEmail())) {
                throw new ForbiddenException("the booking email must be the same as the student's email");
            }
        }
        if (jwtUtils.getRoles().contains(ROLE_LECTURER.getRole())) {
            User lecturer = userRepo.findByUserEmail(jwtUtils.getEmail());
            List<Integer> list = lecturer.getOwnCategories()
                    .stream()
                    .map(EventCategory::getId)
                    .toList();
            if (!list.contains(booking.getEventCategory().getId())) {
                throw new ForbiddenException("this lecturer does not own this category id: " + booking.getEventCategory().getId());
            }
        }
        log.info("Fetching booking id: " + id);
        return modelMapper.map(booking, BookingDetailsDto.class);
    }

    @Override
    public void save(@NotNull BookingDto newBooking) {
        log.info("Saving a new booking...");
        if (isOverlap(newBooking.getCategoryId(), newBooking.getEventStartTime(), newBooking.getEventDuration())) {
            throw new UnprocessableException(newBooking.getEventStartTime() + " is overlap");
        }
        if (jwtUtils.getRoles().contains(ROLE_STUDENT.getRole())) {
            if (!jwtUtils.getEmail().equals(newBooking.getBookingEmail())) {
                throw new IllegalArgumentException("the booking email must be the same as the student's email");
            }
        }
        EventCategory category = categoryRepo
                .findById(newBooking.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("category id " + newBooking.getCategoryId() + " is not found"));
        EventBooking booking = new EventBooking();
        booking.setEventCategory(category);
        booking.setBookingName(newBooking.getBookingName());
        booking.setBookingEmail(newBooking.getBookingEmail());
        booking.setEventDuration(newBooking.getEventDuration());
        booking.setEventStartTime(newBooking.getEventStartTime());
        booking.setEventNotes(newBooking.getEventNotes());
        bookingRepo.saveAndFlush(booking);
    }

    @Override
    public void delete(Integer id) {
        if (!bookingRepo.existsById(id)) throw new ResourceNotFoundException("booking id " + id + " is not found");
        EventBooking booking = bookingRepo.getEventBookingById(id);
        if (jwtUtils.getRoles().contains(ROLE_STUDENT.getRole())) {
            if (!jwtUtils.getEmail().equals(booking.getBookingEmail())) {
                throw new ForbiddenException("the booking email must be the same as the student's email");
            }
        }
        log.info("Deleting booking id: " + id);
        bookingRepo.deleteById(id);
    }

    @Override
    public BookingDetailsDto update(Integer id, @NotNull Map<String, Object> changes) {
        if (jwtUtils.getRoles().contains(ROLE_STUDENT.getRole())) {
            EventBooking booking = bookingRepo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("booking id " + id + " is not found"));
            if (!jwtUtils.getEmail().equals(booking.getBookingEmail())) {
                throw new ForbiddenException("the booking email must be the same as the student's email");
            }
        }
        log.info("Updating field " + changes.keySet() + " of booking id: " + id);
        return bookingRepo.findById(id)
                .map(booking -> mapBooking(booking, changes))
                .map(bookingRepo::saveAndFlush)
                .map(updatedBooking -> modelMapper.map(updatedBooking, BookingDetailsDto.class))
                .orElseThrow(() -> new ResourceNotFoundException("booking id " + id + " is not found"));
    }

    @Override
    public List<BookingViewDto> getEventsByDate(String date) {
        log.info("Fetching all bookings by date: " + date);
        List<EventBooking> bookings = bookingRepo.findAllByByDate(date);
        return listMapper.mapList(bookings, BookingViewDto.class, modelMapper);
    }

    private EventBooking mapBooking(EventBooking booking, Map<String, Object> changes) {
        Integer id = booking.getId();
        changes.forEach((field, value) -> {
            switch (field) {
                case "eventStartTime" -> {
                    if (value == null) throw new IllegalArgumentException(field + " is must not be null");
                    final String TIME_ZONE = "GMT+07:00";
                    LocalDateTime dateTime = LocalDateTime.parse((String) value);
                    if (dateTime.isBefore(LocalDateTime.now(ZoneId.of(TIME_ZONE))))
                        throw new IllegalArgumentException(field + " is must be a date and time in the future");
                    Integer duration = bookingRepo.getEventDurationById(id);
                    Integer categoryId = bookingRepo.getEventCategoryIdById(id);
                    if (isOverlap(id, categoryId, dateTime, duration))
                        throw new UnprocessableException(dateTime + " is overlap");
                    log.info("Updating start time of id: " + id);
                    booking.setEventStartTime(dateTime);
                }
                case "eventNotes" -> {
                    String notes = (String) value;
                    if (notes != null && notes.length() > 500)
                        throw new IllegalArgumentException("size must be between 0 and 500");
                    log.info("Updating notes of id: " + id);
                    booking.setEventNotes(notes == null ? null : notes.trim());
                }
                default -> throw new IllegalArgumentException("Unknown field: " + field);
            }
        });
        return booking;
    }

    private boolean isOverlap(Integer id, Integer categoryId, LocalDateTime dateTime, Integer duration) {
        LocalTime startA = getStartTime(dateTime);
        LocalTime endA = getEndTime(dateTime, duration);
        String date = dateTime.toLocalDate().toString();
        List<EventBooking> bookings = bookingRepo.findAllByDateAndCategory(date, categoryId, id);
        return checkOverlap(startA, endA, bookings);
    }

    private boolean isOverlap(Integer categoryId, LocalDateTime dateTime, Integer duration) {
        LocalTime startA = getStartTime(dateTime);
        LocalTime endA = getEndTime(dateTime, duration);
        String date = dateTime.toLocalDate().toString();
        List<EventBooking> bookings = bookingRepo.findAllByDateAndCategory(date, categoryId);
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

    private LocalDateTime getEndDateTime(BookingViewDto booking) {
        LocalDateTime startDateTime = LocalDateTime.parse(booking.getStartDateTime());
        return startDateTime.plusMinutes(booking.getEventDuration());
    }
}
