package sit.int221.oasipservice.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

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

    @Column(name = "user_password", nullable = false, length = 100)
    private String userPassword;

    @CreationTimestamp
    @Column(name = "createdOn",
            nullable = false,
            insertable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private OffsetDateTime createdOn;

    @UpdateTimestamp
    @Column(name = "updatedOn",
            nullable = false,
            insertable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private OffsetDateTime updatedOn;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> userRoles = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "event_category_owner",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<EventCategory> ownCategories = new LinkedHashSet<>();

    public void addCategory(EventCategory category) {
        this.ownCategories.add(category);
        category.getOwners().add(this);
    }

    public void removeCategory(Integer categoryId) {
        EventCategory category = this.ownCategories
                .stream()
                .filter(c -> Objects.equals(c.getId(), categoryId))
                .findFirst().orElse(null);
        if (category != null) {
            this.ownCategories.remove(category);
            category.getOwners().remove(this);
        }
    }

    public void addRole(Role role) {
        this.userRoles.add(role);
    }

    public void updateRole(Role role) {
        this.userRoles.clear();
        this.userRoles.add(role);
    }
}