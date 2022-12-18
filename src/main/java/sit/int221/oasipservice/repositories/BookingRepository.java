package sit.int221.oasipservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sit.int221.oasipservice.entities.EventBooking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<EventBooking, Integer> {
    @Query(value = "SELECT * FROM event_booking " +
            "WHERE date(event_start_time) = ?1 " +
            "AND event_category = ?2 " +
            "AND booking_id != ?3 " +
            "ORDER BY time(event_start_time)", nativeQuery = true)
    List<EventBooking> findAllByDateAndCategory(String date, Integer categoryId, Integer id);

    @Query(value = "SELECT * FROM event_booking " +
            "WHERE date(event_start_time) = ?1 " +
            "AND event_category = ?2 " +
            "ORDER BY time(event_start_time)", nativeQuery = true)
    List<EventBooking> findAllByDateAndCategory(String date, Integer categoryId);

    @Query(value = "SELECT event_duration " +
            "FROM event_booking WHERE booking_id = ?1", nativeQuery = true)
    Integer getEventDurationById(Integer id);

    @Query(value = "SELECT event_category " +
            "FROM event_booking WHERE booking_id = ?1", nativeQuery = true)
    Integer getEventCategoryIdById(Integer id);

    @Query(value = "SELECT * FROM event_booking " +
            "WHERE date(event_start_time) = ?1 " +
            "ORDER BY event_start_time", nativeQuery = true)
    List<EventBooking> findAllByByDate(String date);

    @Query(value = "SELECT * FROM event_booking " +
            "WHERE DATE_ADD(event_start_time, INTERVAL event_duration MINUTE) < now() " +
            "ORDER BY event_start_time DESC", nativeQuery = true)
    List<EventBooking> getPastEvents();

    @Query(value = "SELECT * FROM event_booking " +
            "WHERE event_category = ?1 " +
            "AND DATE_ADD(event_start_time, INTERVAL event_duration MINUTE) < now() " +
            "ORDER BY event_start_time DESC", nativeQuery = true)
    List<EventBooking> getPastEventsByDateAndCategory(Integer categoryId);

    @Query(value = "SELECT * FROM event_booking " +
            "WHERE DATE_ADD(event_start_time, INTERVAL event_duration MINUTE) > now() " +
            "ORDER BY event_start_time", nativeQuery = true)
    List<EventBooking> getFutureEvents();

    @Query(value = "SELECT * FROM event_booking " +
            "WHERE event_category = ?1 " +
            "AND DATE_ADD(event_start_time, INTERVAL event_duration MINUTE) > now() " +
            "ORDER BY event_start_time", nativeQuery = true)
    List<EventBooking> getFutureEventsByDateAndCategory(Integer categoryId);

    @Query(value = "SELECT * FROM event_booking " +
            "WHERE booking_email = ?1 " +
            "ORDER BY event_start_time DESC", nativeQuery = true)
    List<EventBooking> findByBookingEmail(String email);

    @Query(value = "SELECT * FROM event_booking " +
            "WHERE booking_email = ?1 " +
            "AND DATE_ADD(event_start_time, INTERVAL event_duration MINUTE) > now() " +
            "ORDER BY event_start_time", nativeQuery = true)
    List<EventBooking> getFutureEvents(String email);

    @Query(value = "SELECT * FROM event_booking " +
            "WHERE booking_email = ?1 " +
            "AND DATE_ADD(event_start_time, INTERVAL event_duration MINUTE) < now() " +
            "ORDER BY event_start_time DESC", nativeQuery = true)
    List<EventBooking> getPastEvents(String email);
}