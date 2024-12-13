package vn.hdl.itjob.config;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import vn.hdl.itjob.repository.UserRepository;

// @Service
@Component("userDetailsService")
public class UserDetailServiceCustom implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailServiceCustom(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        vn.hdl.itjob.domain.User user = this.userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User with email " + username + " was not found in the database"));

        String role = user.getRole() != null ? user.getRole().getName() : "NULL";

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(role)));
    }

}
