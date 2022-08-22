package sit.int221.oasipservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;

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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "createdOn", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdOn;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "updatedOn", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime updatedOn;
}