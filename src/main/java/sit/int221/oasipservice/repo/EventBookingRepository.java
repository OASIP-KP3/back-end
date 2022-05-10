package sit.int221.oasipservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sit.int221.oasipservice.entities.EventBooking;

import java.util.List;

public interface EventBookingRepository extends JpaRepository<EventBooking, Integer> {
    @Query(value = "SELECT * FROM event_booking " +
            "WHERE date(event_start_time) = ?1 AND event_category = ?2 " +
            "ORDER BY time(event_start_time)", nativeQuery = true)
    List<EventBooking> findAllByDateAndCategory(String date, Integer categoryId);
}