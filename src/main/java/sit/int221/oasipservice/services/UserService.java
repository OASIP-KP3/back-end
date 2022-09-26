package sit.int221.oasipservice.services;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import sit.int221.oasipservice.dto.users.UserDetailsDto;
import sit.int221.oasipservice.dto.users.UserDto;
import sit.int221.oasipservice.dto.users.UserPageDto;
import sit.int221.oasipservice.exceptions.UnauthorizedException;
import sit.int221.oasipservice.exceptions.UnprocessableException;
import sit.int221.oasipservice.payload.request.LoginRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface UserService {
    void save(UserDto newUser);

    UserPageDto getUsers(int page, int pageSize, String sortBy);

    void delete(Integer id) throws ResourceNotFoundException;

    UserDetailsDto getUser(Integer id) throws ResourceNotFoundException;

    UserDetailsDto update(Integer id, Map<String, Object> changes) throws ResourceNotFoundException, UnprocessableException, IllegalArgumentException;

    void addRoleToUser(String email, String roleName);

    void refreshToken(HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<String> matchPassword(LoginRequest request) throws ResourceNotFoundException, UnauthorizedException;
}
