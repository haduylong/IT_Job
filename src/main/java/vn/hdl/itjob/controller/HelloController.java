package vn.hdl.itjob.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @GetMapping
    public String HelloItJob(@AuthenticationPrincipal OidcUser oidcUser) {
        return "Hello %s, email: %s".formatted(oidcUser.getFullName(), oidcUser.getEmail());
        // return "Hello, Welcome to IT Job";
    }
}
