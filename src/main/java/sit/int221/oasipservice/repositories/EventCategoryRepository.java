package sit.int221.oasipservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sit.int221.oasipservice.entities.EventCategory;

import java.util.Set;

@Repository
public interface EventCategoryRepository extends JpaRepository<EventCategory, Integer> {
    @Query(value = "SELECT category_name FROM event_category", nativeQuery = true)
    Set<String> getAllCategoryName();
}