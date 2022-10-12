package sit.int221.oasipservice.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sit.int221.oasipservice.dto.categories.CategoryDto;
import sit.int221.oasipservice.dto.users.UserDetailsDto;
import sit.int221.oasipservice.dto.users.UserPageDto;
import sit.int221.oasipservice.entities.Role;
import sit.int221.oasipservice.entities.User;
import sit.int221.oasipservice.exceptions.UnauthorizedException;
import sit.int221.oasipservice.exceptions.UnprocessableException;
import sit.int221.oasipservice.payload.request.LoginRequest;
import sit.int221.oasipservice.repositories.RoleRepository;
import sit.int221.oasipservice.repositories.UserRepository;
import sit.int221.oasipservice.services.UserService;
import sit.int221.oasipservice.utils.ListMapper;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final ListMapper listMapper;

    @Override
    public UserPageDto getUsers(int page, int pageSize, String sortBy) {
        log.info("Fetching all users...");
        Sort sort = Sort.by(sortBy).ascending();
        return modelMapper.map(userRepo.findAll(PageRequest.of(page, pageSize, sort)), UserPageDto.class);
    }

    @Override
    public void delete(Integer id) throws ResourceNotFoundException {
        log.info("Deleting user id " + id);
        if (!userRepo.existsById(id)) throw new ResourceNotFoundException("ID " + id + " is not found");
        userRepo.deleteById(id);
    }

    @Override
    public UserDetailsDto getUser(Integer id) throws ResourceNotFoundException {
        log.info("Fetching the details of user id " + id);
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        return modelMapper.map(user, UserDetailsDto.class);
    }

    @Override
    public List<CategoryDto> getCategoriesByUserId(Integer id) throws ResourceNotFoundException {
        log.info("Fetching the categories by user id " + id);
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        return listMapper.mapList(user.getOwnCategories().stream().toList(), CategoryDto.class, modelMapper);
    }

    @Override
    public UserDetailsDto update(Integer id, Map<String, Object> changes) throws ResourceNotFoundException, UnprocessableException, IllegalArgumentException {
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        changes.forEach((field, value) -> {
            switch (field) {
                case "userName" -> {
                    String username = (String) value;
                    if (username == null || username.isBlank())
                        throw new IllegalArgumentException(field + " is must not be null or empty");
                    if (!isUsernameUnique(user.getUserName(), username))
                        throw new UnprocessableException("Username " + username + " is not unique");
                    log.info("Updating the username id " + id);
                    user.setUserName(username.trim());
                }
                case "userEmail" -> {
                    String email = (String) value;
                    if (email == null || email.isBlank())
                        throw new IllegalArgumentException(field + " is must not be null or empty");
                    if (!isEmailValid(email)) throw new UnprocessableException(email + " is not valid");
                    if (!isEmailUnique(user.getUserEmail(), email))
                        throw new UnprocessableException(email + " is not unique");
                    log.info("Updating the email of user id " + id);
                    user.setUserEmail(email.toLowerCase(Locale.US));
                }
                case "userRole" -> {
                    String role = (String) value;
                    if (role == null || role.isBlank())
                        throw new IllegalArgumentException(field + " is must not be null or empty");
                    if (roleRepo.findByRoleName(role) == null)
                        throw new UnprocessableException(role + " is not defined");
                    final String ROLE = role.trim().toLowerCase(Locale.US);
                    log.info("Updating the role of user id " + id);
                    user.updateRole(roleRepo.findByRoleName(ROLE));
                }
                default -> throw new IllegalArgumentException("Unknown field: " + field);
            }
        });
        return modelMapper.map(userRepo.saveAndFlush(user), UserDetailsDto.class);
    }

    @Override
    public void addRoleToUser(String email, String roleName) {
        User user = userRepo.findByUserEmail(email);
        Role role = roleRepo.findByRoleName(roleName);
        log.info("Adding role " + roleName + " to user id " + user.getId());
        user.getUserRoles().add(role);
    }

    @Override
    public ResponseEntity<String> matchPassword(LoginRequest request) throws ResourceNotFoundException, UnauthorizedException {
        String email = request.getUserEmail();
        String password = request.getUserPassword();
        User user = userRepo.findByUserEmail(email);
        if (user == null) throw new ResourceNotFoundException(email + " is not found");
        if (!passwordEncoder.matches(password, user.getUserPassword()))
            throw new UnauthorizedException("Password is incorrect");
        return ResponseEntity.ok().body("Password is matched");
    }

    private boolean isUsernameUnique(String oldUsername, String newUsername) {
        for (String username : userRepo.filterUsernameOutBy(oldUsername)) {
            if (newUsername.equals(username)) return false;
        }
        return true;
    }

    private boolean isEmailUnique(String oldEmail, String newEmail) {
        for (String email : userRepo.filterEmailOutBy(oldEmail)) {
            if (newEmail.equals(email)) return false;
        }
        return true;
    }

    private boolean isEmailValid(String email) {
        String regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexp).matcher(email).matches();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByUserEmail(email);
        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found");
        } else {
            log.info("User found in the database");
        }
        List<SimpleGrantedAuthority> authorities = user.getUserRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUserEmail(), user.getUserPassword(), authorities);
    }
}
