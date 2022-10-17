package sit.int221.oasipservice.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sit.int221.oasipservice.entities.User;
import sit.int221.oasipservice.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUserEmail(username);
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
