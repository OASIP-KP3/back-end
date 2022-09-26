package sit.int221.oasipservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sit.int221.oasipservice.entities.User;
import sit.int221.oasipservice.exceptions.UnauthorizedException;
import sit.int221.oasipservice.payload.request.LoginRequest;
import sit.int221.oasipservice.payload.response.JwtResponse;
import sit.int221.oasipservice.repositories.UserRepository;
import sit.int221.oasipservice.utils.JwtUtil;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepo;
    private final UserDetailsService userService;
    private final JwtUtil jwtUtil;

    public JwtResponse login(LoginRequest request) throws ResourceNotFoundException, UnauthorizedException {
        String email = request.getUserEmail();
        String password = request.getUserPassword();
        log.info("Attempting to authenticate with " + email);
        User user = userRepo.findByUserEmail(email);
        if (user == null) throw new ResourceNotFoundException(email + " is not found");
        if (!passwordEncoder.matches(password, user.getUserPassword()))
            throw new UnauthorizedException("Password is incorrect");
        authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        return jwtUtil.generateToken(userService.loadUserByUsername(email));
    }
}
