package sit.int221.oasipservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sit.int221.oasipservice.dto.users.UserDetailsDto;
import sit.int221.oasipservice.dto.users.UserPageDto;
import sit.int221.oasipservice.payload.request.LoginRequest;
import sit.int221.oasipservice.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/users")
@RequiredArgsConstructor
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

    @GetMapping("/{id}")
    public UserDetailsDto getUser(@PathVariable Integer id) {
        return service.getUser(id);
    }

    @DeleteMapping("/{id}")
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

    @PostMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        service.refreshToken(request, response);
    }
}
