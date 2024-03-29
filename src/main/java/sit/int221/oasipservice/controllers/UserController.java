package sit.int221.oasipservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.int221.oasipservice.dto.categories.CategoryDto;
import sit.int221.oasipservice.dto.users.UserDetailsDto;
import sit.int221.oasipservice.dto.users.UserPageDto;
import sit.int221.oasipservice.payload.request.LoginRequest;
import sit.int221.oasipservice.services.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/users")
public class UserController {
    private final UserService service;

    // default sorting by "userName" in ascending
    @GetMapping("")
    public UserPageDto getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "userName") String sortBy) {
        return service.getUsers(page, pageSize, sortBy);
    }

    @GetMapping("/{id}/categories")
    public List<CategoryDto> getCategoriesByUserId(@PathVariable Integer id) {
        return service.getCategoriesByUserId(id);
    }

    @GetMapping("/{id}")
    public UserDetailsDto getUser(@PathVariable Integer id) {
        return service.getUser(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteUser(@PathVariable Integer id) {
        service.delete(id);
    }

    @PatchMapping("/{id}")
    public UserDetailsDto partialUpdateUser(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        return service.update(id, body);
    }

    @PostMapping("/match")
    public ResponseEntity<String> matchPassword(@Valid @RequestBody LoginRequest request) {
        return service.matchPassword(request);
    }
}
