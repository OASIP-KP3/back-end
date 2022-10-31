package sit.int221.oasipservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sit.int221.oasipservice.entities.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserName(String userName);

    User findByUserEmail(String email);

    @Query(value = "SELECT user_name FROM user WHERE user_name <> ?1", nativeQuery = true)
    List<String> filterUserNameOutBy(String userName);

    @Query(value = "SELECT user_email FROM user WHERE user_email <> ?1", nativeQuery = true)
    List<String> filterEmailOutBy(String email);
}
