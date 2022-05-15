package sit.int221.oasipservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sit.int221.oasipservice.entities.EventBooking;

import java.util.List;

public interface EventBookingRepository extends JpaRepository<EventBooking, Integer> {
    @Query(value = "SELECT * FROM event_booking " +
            "WHERE date(event_start_time) = ?1 " +
            "AND event_category = ?2 " +
            "AND hour(event_start_time) >= ?3 " +
            "ORDER BY time(event_start_time)", nativeQuery = true)
    List<EventBooking> findAllByDateAndCategory(String date, Integer categoryId, Integer hours);

    @Query(value = "SELECT event_duration " +
            "FROM event_booking WHERE booking_id = ?1", nativeQuery = true)
    Integer getEventDurationById(Integer id);

    @Query(value = "SELECT event_category " +
            "FROM event_booking WHERE booking_id = ?1", nativeQuery = true)
    Integer getEventCategoryIdById(Integer id);
}