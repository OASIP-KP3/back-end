package sit.int221.oasipservice.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    private final String TIME_ZONE = "GMT+07:00";

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

    @Column(name = "createdOn", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "updatedOn", nullable = false)
    private LocalDateTime updatedOn;

    @PrePersist
    public void setCreatedOn() {
        this.createdOn = LocalDateTime.now(ZoneId.of(TIME_ZONE));
    }

    @PreUpdate
    public void setUpdatedOn() {
        this.updatedOn = LocalDateTime.now(ZoneId.of(TIME_ZONE));
    }
}