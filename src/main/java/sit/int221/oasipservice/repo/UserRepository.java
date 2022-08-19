package sit.int221.oasipservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int221.oasipservice.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserName(String username);

    User findByUserEmail(String email);
}