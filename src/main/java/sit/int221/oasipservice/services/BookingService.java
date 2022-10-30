package sit.int221.oasipservice.services;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import sit.int221.oasipservice.dto.bookings.BookingDetailsDto;
import sit.int221.oasipservice.dto.bookings.BookingDto;
import sit.int221.oasipservice.dto.bookings.BookingViewDto;
import sit.int221.oasipservice.exceptions.UnprocessableException;

import java.util.List;
import java.util.Map;

public interface BookingService {
    List<BookingViewDto> getEvents(String sortBy, String type) throws IllegalArgumentException;

    BookingDetailsDto getEvent(Integer id) throws ResourceNotFoundException;

    void save(BookingDto newBooking) throws ResourceNotFoundException, UnprocessableException;

    void delete(Integer id) throws ResourceNotFoundException;

    BookingDetailsDto update(Integer id, Map<String, Object> changes) throws ResourceNotFoundException;

    List<BookingViewDto> getEventsByDate(String date);
}
