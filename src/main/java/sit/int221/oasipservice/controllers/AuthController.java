package sit.int221.oasipservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sit.int221.oasipservice.dto.jwt.JwtResponseDto;
import sit.int221.oasipservice.dto.users.UserDto;
import sit.int221.oasipservice.dto.users.UserLoginDto;
import sit.int221.oasipservice.services.MyUserDetailsService;
import sit.int221.oasipservice.utils.JwtUtil;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v2/auth")
public class AuthController {
    private final MyUserDetailsService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(MyUserDetailsService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public JwtResponseDto login(@Valid @RequestBody UserLoginDto body) {
        return userService.login(body);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@Valid @RequestBody UserDto newUser) {
        userService.save(newUser);
    }

//    @PostMapping("/refresh")
//    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequestDto request) {
//        return null;
//    }
}
