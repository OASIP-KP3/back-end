package sit.int221.oasipservice.services;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.ServletRequestBindingException;
import sit.int221.oasipservice.exceptions.UnauthorizedException;
import sit.int221.oasipservice.payload.request.LoginRequest;
import sit.int221.oasipservice.payload.request.RegisterRequest;
import sit.int221.oasipservice.payload.response.JwtResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthService {
    JwtResponse login(LoginRequest request) throws ResourceNotFoundException, UnauthorizedException;

    JwtResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws
            ResourceNotFoundException,
            TokenExpiredException,
            JWTDecodeException,
            ServletRequestBindingException;

    void save(RegisterRequest newUser);
}
