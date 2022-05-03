package sit.int221.oasipservice.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "event_category", indexes = {
        @Index(name = "PRIMARY", columnList = "category_id"),
        @Index(name = "category_name_UNIQUE", columnList = "category_name")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uc_category_name", columnNames = {"category_name"})
})
public class EventCategory {
    @Id
    @Column(name = "category_id", nullable = false)
    private Integer id;

    @Column(name = "category_name", nullable = false, length = 100)
    private String categoryName;

    @Column(name = "category_description", length = 500)
    private String categoryDescription;

    @Column(name = "event_duration", nullable = false)
    private Integer eventDuration;

    @OneToMany(mappedBy = "eventCategory")
    private Set<EventBooking> eventBookings = new LinkedHashSet<>();
}