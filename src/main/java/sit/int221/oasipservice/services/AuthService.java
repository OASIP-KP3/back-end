package sit.int221.oasipservice.services;

import org.springframework.web.bind.ServletRequestBindingException;
import sit.int221.oasipservice.payload.request.LoginRequest;
import sit.int221.oasipservice.payload.request.RegisterRequest;
import sit.int221.oasipservice.payload.response.JwtResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {
    JwtResponse login(LoginRequest request);

    JwtResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException;

    void save(RegisterRequest newUser);
}
