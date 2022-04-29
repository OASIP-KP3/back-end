package sit.int221.oasipservice.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "event_booking")
public class EventBooking {
    @Id
    @Column(name = "booking_id", nullable = false)
    private Integer id;

    @Column(name = "booking_name", nullable = false, length = 100)
    private String bookingName;

    @Column(name = "booking_email", nullable = false, length = 45)
    private String bookingEmail;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "event_category", nullable = false)
    private EventCategory eventCategory;

    @Column(name = "event_start_time", nullable = false)
    private Instant eventStartTime;

    @Column(name = "event_duration", nullable = false)
    private Integer eventDuration;

    @Column(name = "event_notes", length = 500)
    private String eventNotes;
}