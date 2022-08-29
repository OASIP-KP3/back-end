package sit.int221.oasipservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sit.int221.oasipservice.dto.users.UserDetailsDto;
import sit.int221.oasipservice.dto.users.UserDto;
import sit.int221.oasipservice.dto.users.UserListPageDto;
import sit.int221.oasipservice.dto.users.UserLoginDto;
import sit.int221.oasipservice.entities.User;
import sit.int221.oasipservice.enumtype.Role;
import sit.int221.oasipservice.exceptions.UnauthorizedException;
import sit.int221.oasipservice.exceptions.UnprocessableException;
import sit.int221.oasipservice.repositories.UserRepository;
import sit.int221.oasipservice.utils.ListMapper;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final UserRepository repo;
    private final ModelMapper modelMapper;
    private final ListMapper listMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repo, ModelMapper modelMapper, ListMapper listMapper, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.modelMapper = modelMapper;
        this.listMapper = listMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserListPageDto getUsers(int page, int pageSize, String sortBy) {
        Sort sort = Sort.by(sortBy).ascending();
        return modelMapper.map(repo.findAll(PageRequest.of(page, pageSize, sort)), UserListPageDto.class);
    }

    public void delete(Integer id) throws ResourceNotFoundException {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("ID " + id + " is not found");
        }
        repo.deleteById(id);
    }

    public UserDetailsDto getUserDetails(Integer id) throws ResourceNotFoundException {
        User user = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        return modelMapper.map(user, UserDetailsDto.class);
    }

    public void save(UserDto newUser) {
        repo.saveAndFlush(populateUser(newUser));
    }

    private User populateUser(UserDto userData) {
        User user = new User();
        final String ROLE = userData.getUserRole().toUpperCase(Locale.US);
        user.setUserName(userData.getUserName());
        user.setUserEmail(userData.getUserEmail());
        user.setUserRole(Role.valueOf(ROLE).getRole());
        user.setUserPassword(passwordEncoder.encode(userData.getUserPassword()));
        return user;
    }

    public ResponseEntity<String> matcher(UserLoginDto body) throws ResourceNotFoundException, UnauthorizedException {
        User user = repo.findByUserEmail(body.getUserEmail());
        if (user == null) {
            throw new ResourceNotFoundException(body.getUserEmail() + " is not found");
        }
        String password = repo.getPasswordByEmail(body.getUserEmail());
        if (!passwordEncoder.matches(body.getUserPassword(), password)) {
            throw new UnauthorizedException("Password is not matched");
        }
        return ResponseEntity.ok("Password is matched");
    }

    public UserDetailsDto update(Integer id, Map<String, Object> changes) throws ResourceNotFoundException, UnprocessableException, IllegalArgumentException {
        User user = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        changes.forEach((field, value) -> {
            switch (field) {
                case "userName" -> {
                    String username = (String) value;
                    if (username == null || username.isBlank()) {
                        throw new IllegalArgumentException(field + " is must not be null or empty");
                    }
                    if (!isUsernameUnique(user.getUserName(), username)) {
                        throw new UnprocessableException("Username " + username + " is not unique");
                    }
                    user.setUserName(username.trim());
                }
                case "userEmail" -> {
                    String email = (String) value;
                    if (email == null || email.isBlank()) {
                        throw new IllegalArgumentException(field + " is must not be null or empty");
                    }
                    if (!isEmailValid(email)) {
                        throw new UnprocessableException(email + " is not valid");
                    }
                    if (!isEmailUnique(user.getUserEmail(), email)) {
                        throw new UnprocessableException(email + " is not unique");
                    }
                    user.setUserEmail(email.toLowerCase(Locale.US));
                }
                case "userRole" -> {
                    String role = (String) value;
                    if (role == null || role.isBlank()) {
                        throw new IllegalArgumentException(field + " is must not be null or empty");
                    }
                    if (!existsByRole(role)) {
                        throw new UnprocessableException(role + " is not defined");
                    }
                    final String ROLE = role.trim().toUpperCase(Locale.US);
                    user.setUserRole(Role.valueOf(ROLE).getRole());
                }
                default -> throw new IllegalArgumentException("Unknown field: " + field);
            }
        });
        return modelMapper.map(repo.saveAndFlush(user), UserDetailsDto.class);
    }

    private boolean isUsernameUnique(String oldUsername, String newUsername) {
        for (String username : repo.filterUsernameOutBy(oldUsername)) {
            if (newUsername.equals(username)) {
                return false;
            }
        }
        return true;
    }

    private boolean isEmailUnique(String oldEmail, String newEmail) {
        for (String email : repo.filterEmailOutBy(oldEmail)) {
            if (newEmail.equals(email)) {
                return false;
            }
        }
        return true;
    }

    private boolean isEmailValid(String email) {
        String regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexp).matcher(email).matches();
    }

    private boolean existsByRole(String userRole) {
        String temp = userRole.trim().toLowerCase();
        for (Role role : Role.values()) {
            if (temp.equals(role.getRole())) {
                return true;
            }
        }
        return false;
    }
}
