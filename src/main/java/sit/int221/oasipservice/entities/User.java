package sit.int221.oasipservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @Column(name = "user_password", nullable = false, length = 100)
    private String userPassword;

    @CreationTimestamp
    @Column(name = "createdOn", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdOn;

    @UpdateTimestamp
    @Column(name = "updatedOn", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime updatedOn;
}