package vn.hdl.itjob.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.hdl.itjob.domain.User;
import vn.hdl.itjob.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {
    private final UserRepository userRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        // User user = this.userRepository.findByEmail(oidcUser.getEmail())
        // .orElseThrow(() -> new UsernameNotFoundException("User not found with google
        // login"));

        User user = this.userRepository.findByEmail(oidcUser.getEmail()).orElse(null);
        if (user == null) { // create if not exist
            user = new User();
            user.setName(oidcUser.getFullName());
            user.setEmail(oidcUser.getEmail());
            user.setPassword("GOOGLE_OAUTH2");
            user = this.userRepository.save(user);
        }

        String role = user.getRole() != null ? user.getRole().getName() : "NULL";
        List<String> roles = Arrays.asList(role, "USER", "ADMIN");
        // Gán vai trò từ database
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(r -> new SimpleGrantedAuthority(r)).toList();

        // List<GrantedAuthority> authorities = Collections.singletonList(new
        // SimpleGrantedAuthority(role));

        // List<GrantedAuthority> authorities = new ArrayList<>();
        // authorities.add(new SimpleGrantedAuthority(role));
        // authorities.add(new SimpleGrantedAuthority("USER"));

        return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }
}
