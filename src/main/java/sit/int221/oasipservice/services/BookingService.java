package sit.int221.oasipservice.services;

import sit.int221.oasipservice.dto.bookings.BookingDetailsDto;
import sit.int221.oasipservice.dto.bookings.BookingDto;
import sit.int221.oasipservice.dto.bookings.BookingViewDto;

import java.util.List;
import java.util.Map;

public interface BookingService {
    List<BookingViewDto> getEvents(String sortBy, String type);

    BookingDetailsDto getEvent(Integer id);

    void save(BookingDto newBooking);

    void delete(Integer id);

    BookingDetailsDto update(Integer id, Map<String, Object> changes);

    List<BookingViewDto> getEventsByDate(String date);
}
