package sit.int221.oasipservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.oasipservice.dto.users.UserDetailsDto;
import sit.int221.oasipservice.dto.users.UserDto;
import sit.int221.oasipservice.dto.users.UserListPageDto;
import sit.int221.oasipservice.entities.User;
import sit.int221.oasipservice.enumtype.Role;
import sit.int221.oasipservice.repo.UserRepository;
import sit.int221.oasipservice.utils.ListMapper;

import java.util.Map;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final UserRepository repo;
    private final ModelMapper modelMapper;
    private final ListMapper listMapper;

    @Autowired
    public UserService(UserRepository repo, ModelMapper modelMapper, ListMapper listMapper) {
        this.repo = repo;
        this.modelMapper = modelMapper;
        this.listMapper = listMapper;
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
        repo.saveAndFlush(modelMapper.map(newUser, User.class));
    }

    public UserDetailsDto update(Integer id, Map<String, Object> changes) throws ResourceNotFoundException, ResponseStatusException, IllegalArgumentException {
        User user = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ID " + id + " is not found"));
        changes.forEach((field, value) -> {
            switch (field) {
                case "userName" -> {
                    String username = (String) value;
                    if (username == null || username.isBlank()) {
                        throw new IllegalArgumentException(field + " is must not be null or empty");
                    }
                    if (!isUsernameUnique(user.getUserName(), username)) {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Username " + username + " is not unique");
                    }
                    user.setUserName(username.trim());
                }
                case "userEmail" -> {
                    String email = (String) value;
                    if (email == null || email.isBlank()) {
                        throw new IllegalArgumentException(field + " is must not be null or empty");
                    }
                    if (!isEmailValid(email)) {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, email + " is not valid");
                    }
                    if (!isEmailUnique(user.getUserEmail(), email)) {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, email + " is not unique");
                    }
                    user.setUserEmail(email.toLowerCase());
                }
                case "userRole" -> {
                    String role = (String) value;
                    if (role == null || role.isBlank()) {
                        throw new IllegalArgumentException(field + " is must not be null or empty");
                    }
                    if (!existsByRole(role)) {
                        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, role + " is not defined");
                    }
                    user.setUserRole(role.trim().toLowerCase());
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
