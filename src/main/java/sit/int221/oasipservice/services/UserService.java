package sit.int221.oasipservice.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import sit.int221.oasipservice.dto.users.UserDetailsDto;
import sit.int221.oasipservice.dto.users.UserDto;
import sit.int221.oasipservice.dto.users.UserListPageDto;
import sit.int221.oasipservice.entities.User;
import sit.int221.oasipservice.repo.UserRepository;
import sit.int221.oasipservice.utils.ListMapper;

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
}
