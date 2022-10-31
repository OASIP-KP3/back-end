package sit.int221.oasipservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sit.int221.oasipservice.entities.EventCategory;

@Repository
public interface CategoryRepository extends JpaRepository<EventCategory, Integer> {
    EventCategory findByCategoryName(String categoryName);
}