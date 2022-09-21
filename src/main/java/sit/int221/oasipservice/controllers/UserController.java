package sit.int221.oasipservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sit.int221.oasipservice.dto.users.UserDetailsDto;
import sit.int221.oasipservice.dto.users.UserListPageDto;
import sit.int221.oasipservice.services.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/v2/users")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    // default sorting by "userName" in ascending
    @GetMapping("")
    public UserListPageDto getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "userName") String sortBy) {
        return service.getUsers(page, pageSize, sortBy);
    }

    @GetMapping("/{id}")
    public UserDetailsDto getUserDetails(@PathVariable Integer id) {
        return service.getUserDetails(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        service.delete(id);
    }

    @PatchMapping("/{id}")
    public UserDetailsDto partialUpdateUser(@PathVariable Integer id, @RequestBody Map<String, Object> body) {
        return service.update(id, body);
    }
}
