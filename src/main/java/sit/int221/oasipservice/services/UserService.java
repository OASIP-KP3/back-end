package sit.int221.oasipservice.services;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import sit.int221.oasipservice.dto.categories.CategoryDto;
import sit.int221.oasipservice.dto.users.UserDetailsDto;
import sit.int221.oasipservice.dto.users.UserPageDto;
import sit.int221.oasipservice.exceptions.UnauthorizedException;
import sit.int221.oasipservice.payload.request.LoginRequest;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserPageDto getUsers(int page, int pageSize, String sortBy);

    void delete(Integer id) throws ResourceNotFoundException;

    UserDetailsDto getUser(Integer id) throws ResourceNotFoundException;

    List<CategoryDto> getCategoriesByUserId(Integer id) throws ResourceNotFoundException;

    UserDetailsDto update(Integer id, Map<String, Object> changes) throws ResourceNotFoundException;

    void addRoleToUser(String email, String roleName);

    ResponseEntity<String> matchPassword(LoginRequest request) throws ResourceNotFoundException, UnauthorizedException;
}
