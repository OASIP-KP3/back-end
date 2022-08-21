package sit.int221.oasipservice.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Getter
@Setter
@Entity
@Table(name = "user", indexes = {
        @Index(name = "PRIMARY", columnList = "user_id"),
        @Index(name = "user_name_UNIQUE", columnList = "user_name"),
        @Index(name = "user_email_UNIQUE", columnList = "user_email")
}, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_name", "user_email"})
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "user_name", nullable = false, length = 100, unique = true)
    private String userName;

    @Column(name = "user_email", nullable = false, length = 50, unique = true)
    private String userEmail;

    @Lob
    @Column(name = "user_role", nullable = false)
    private String userRole;

    @CreationTimestamp
    @Column(name = "createdOn", nullable = false)
    private OffsetDateTime createdOn;

    @UpdateTimestamp
    @Column(name = "updatedOn", nullable = false)
    private OffsetDateTime updatedOn;

    @PrePersist
    public void setCreatedOn() {
        final String TIME_ZONE = "GMT+07:00";
        this.createdOn = OffsetDateTime.now(ZoneId.of(TIME_ZONE));
    }

    @PreUpdate
    public void setUpdatedOn() {
        final String TIME_ZONE = "GMT+07:00";
        this.updatedOn = OffsetDateTime.now(ZoneId.of(TIME_ZONE));
    }
}