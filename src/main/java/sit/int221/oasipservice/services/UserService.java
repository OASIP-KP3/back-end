package sit.int221.oasipservice.services;

import org.springframework.http.ResponseEntity;
import sit.int221.oasipservice.dto.users.UserDetailsDto;
import sit.int221.oasipservice.dto.users.UserDto;
import sit.int221.oasipservice.dto.users.UserPageDto;
import sit.int221.oasipservice.payload.request.LoginRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface UserService {
    void save(UserDto newUser);

    UserPageDto getUsers(int page, int pageSize, String sortBy);

    void delete(Integer id);

    UserDetailsDto getUser(Integer id);

    UserDetailsDto update(Integer id, Map<String, Object> changes);

    void addRoleToUser(String email, String roleName);

    void refreshToken(HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<String> matchPassword(LoginRequest request);
}
