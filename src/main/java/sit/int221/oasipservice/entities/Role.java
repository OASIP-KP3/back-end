package sit.int221.oasipservice.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "role", indexes = {
        @Index(name = "PRIMARY", columnList = "role_id"),
        @Index(name = "role_name_UNIQUE", columnList = "role_name")
}, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"role_name"})
})
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private Integer id;

    @Column(name = "role_name", nullable = false, length = 15, unique = true)
    private String roleName;

    @Override
    public String toString() {
        return this.roleName;
    }
}