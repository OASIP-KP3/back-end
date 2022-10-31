package sit.int221.oasipservice.services;

import org.springframework.http.ResponseEntity;
import sit.int221.oasipservice.dto.categories.CategoryDto;
import sit.int221.oasipservice.dto.users.UserDetailsDto;
import sit.int221.oasipservice.dto.users.UserPageDto;
import sit.int221.oasipservice.payload.request.LoginRequest;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserPageDto getUsers(int page, int pageSize, String sortBy);

    void delete(Integer id);

    UserDetailsDto getUser(Integer id);

    List<CategoryDto> getCategoriesByUserId(Integer id);

    UserDetailsDto update(Integer id, Map<String, Object> changes);

    void addRoleToUser(String email, String roleName);

    ResponseEntity<String> matchPassword(LoginRequest request);
}
