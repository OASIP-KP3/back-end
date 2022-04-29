package sit.int221.oasipservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.oasipservice.entities.EventBooking;

public interface EventBookingRepository extends JpaRepository<EventBooking, Integer> {
}