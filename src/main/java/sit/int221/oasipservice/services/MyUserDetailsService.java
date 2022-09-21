package sit.int221.oasipservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sit.int221.oasipservice.dto.jwt.JwtResponseDto;
import sit.int221.oasipservice.dto.users.UserDto;
import sit.int221.oasipservice.dto.users.UserLoginDto;
import sit.int221.oasipservice.enumtype.Role;
import sit.int221.oasipservice.exceptions.UnauthorizedException;
import sit.int221.oasipservice.repositories.UserRepository;
import sit.int221.oasipservice.utils.JwtUtil;

import java.util.ArrayList;
import java.util.Locale;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public MyUserDetailsService(UserRepository repo, @Lazy PasswordEncoder passwordEncoder, JwtUtil jwtUtil, @Lazy AuthenticationManager authManager) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        sit.int221.oasipservice.entities.User user = repo.findByUserEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new User(user.getUserEmail(), user.getUserPassword(), new ArrayList<>());
    }

    public JwtResponseDto login(UserLoginDto body) throws ResourceNotFoundException, UnauthorizedException {
        sit.int221.oasipservice.entities.User user = repo.findByUserEmail(body.getUserEmail());
        if (user == null) {
            throw new ResourceNotFoundException(body.getUserEmail() + " is not found");
        }
        String password = repo.getPasswordByEmail(body.getUserEmail());
        if (!passwordEncoder.matches(body.getUserPassword(), password)) {
            throw new UnauthorizedException("Incorrect password");
        }
        return new JwtResponseDto(generateToken(body));
    }

    public void save(UserDto newUser) {
        repo.saveAndFlush(populateUser(newUser));
    }

    private sit.int221.oasipservice.entities.User populateUser(UserDto userData) {
        sit.int221.oasipservice.entities.User user = new sit.int221.oasipservice.entities.User();
        final String ROLE = userData.getUserRole().toUpperCase(Locale.US);
        user.setUserName(userData.getUserName());
        user.setUserEmail(userData.getUserEmail());
        user.setUserRole(Role.valueOf(ROLE).getRole());
        user.setUserPassword(passwordEncoder.encode(userData.getUserPassword()));
        return user;
    }

    private String generateToken(UserLoginDto user) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserEmail(), user.getUserPassword()));
        final UserDetails USER_DETAILS = loadUserByUsername(user.getUserEmail());
        return jwtUtil.generateToken(USER_DETAILS);
    }
}
