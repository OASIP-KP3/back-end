package sit.int221.oasipservice.services;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.oasipservice.dto.bookings.BookingDetailsDto;
import sit.int221.oasipservice.dto.bookings.BookingDto;
import sit.int221.oasipservice.dto.bookings.BookingViewDto;

import java.util.List;
import java.util.Map;

public interface BookingService {
    List<BookingViewDto> getEvents(String sortBy, String type) throws IllegalArgumentException;

    BookingDetailsDto getEvent(Integer id) throws ResourceNotFoundException;

    void save(BookingDto newBooking) throws ResponseStatusException;

    void delete(Integer id) throws ResourceNotFoundException;

    BookingDetailsDto update(Integer id, Map<String, Object> changes) throws ResourceNotFoundException, ResponseStatusException, IllegalArgumentException;

    List<BookingViewDto> getEventsByDate(String date);
}
