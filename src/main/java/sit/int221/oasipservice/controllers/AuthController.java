package sit.int221.oasipservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sit.int221.oasipservice.payload.request.LoginRequest;
import sit.int221.oasipservice.payload.request.RegisterRequest;
import sit.int221.oasipservice.payload.response.JwtResponse;
import sit.int221.oasipservice.services.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/login")
    public JwtResponse login(@Valid @RequestBody LoginRequest request) {
        return service.login(request);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest newUser) {
        service.save(newUser);
    }

    @PostMapping("/logout")
    public void logout() {
    }
}
