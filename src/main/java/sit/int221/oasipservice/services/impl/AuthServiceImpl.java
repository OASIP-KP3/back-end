package sit.int221.oasipservice.services.impl;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestBindingException;
import sit.int221.oasipservice.entities.User;
import sit.int221.oasipservice.exceptions.UnauthorizedException;
import sit.int221.oasipservice.payload.request.LoginRequest;
import sit.int221.oasipservice.payload.request.RegisterRequest;
import sit.int221.oasipservice.payload.response.JwtResponse;
import sit.int221.oasipservice.repositories.RoleRepository;
import sit.int221.oasipservice.repositories.UserRepository;
import sit.int221.oasipservice.services.AuthService;
import sit.int221.oasipservice.utils.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final UserDetailsService userService;
    private final JwtUtils jwtUtils;

    @Override
    public JwtResponse login(@NotNull LoginRequest request) throws ResourceNotFoundException, UnauthorizedException {
        String email = request.getUserEmail();
        String password = request.getUserPassword();
        User user = userRepo.findByUserEmail(email);
        if (user == null) {
            String errorMessage = email + " is not found";
            log.error(errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }
        if (!passwordEncoder.matches(password, user.getUserPassword())) {
            String errorMessage = "Password is incorrect";
            log.error(errorMessage);
            throw new UnauthorizedException(errorMessage);
        }
        authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        log.info("Attempting to authenticate with " + email);
        return jwtUtils.generateToken(userService.loadUserByUsername(email));
    }

    @Override
    public JwtResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws
            ResourceNotFoundException,
            TokenExpiredException,
            JWTDecodeException,
            ServletRequestBindingException {
        if (!jwtUtils.isHeaderValid(request)) {
            String errorMessage = "Invalid header value or missing authorization header";
            log.error(errorMessage);
            response.setHeader("Error", errorMessage);
            throw new ServletRequestBindingException(errorMessage);
        }
        String refreshToken = jwtUtils.getToken(request);
        String email = jwtUtils.getEmail(refreshToken);
        if (refreshToken == null || email == null) {
            String errorMessage = "Invalid refresh token";
            log.error(errorMessage);
            response.setHeader("Error", errorMessage);
            throw new JWTDecodeException(errorMessage);
        }
        if (jwtUtils.isTokenExpired(refreshToken)) {
            String errorMessage = "The Token has expired on ";
            log.error(errorMessage + jwtUtils.getExpiresAt(refreshToken).toString());
            response.setHeader("Error", errorMessage);
            throw new TokenExpiredException(errorMessage, jwtUtils.getExpiresAt(refreshToken));
        }
        User user = userRepo.findByUserEmail(email);
        if (user == null) {
            String errorMessage = email + " is not found";
            log.error(errorMessage);
            response.setHeader("Error", errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        }
        String tokenType = "Bearer";
        String accessToken = jwtUtils.generateAccessToken(userService.loadUserByUsername(email));
        log.info("Renewing access token for user id " + user.getId());
        return new JwtResponse(accessToken, refreshToken, tokenType);
    }

    @Override
    public void save(@NotNull RegisterRequest newUser) {
        log.info("Saving a new user to the database...");
        userRepo.saveAndFlush(populateUser(newUser));
    }

    private User populateUser(RegisterRequest userData) {
        User user = new User();
        user.setUserName(userData.getUserName());
        user.setUserEmail(userData.getUserEmail());
        user.addRole(roleRepo.findByRoleName(userData.getUserRole()));
        user.setUserPassword(passwordEncoder.encode(userData.getUserPassword()));
        return user;
    }
}
