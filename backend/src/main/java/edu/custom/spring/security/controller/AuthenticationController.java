package edu.custom.spring.security.controller;

import edu.custom.spring.security.model.security.User;
import edu.custom.spring.security.security.authentication.credentials.BasicAuthenticationPayload;
import edu.custom.spring.security.security.service.SecurityUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final SecurityUserDetailsService securityUserDetailsService;

    public AuthenticationController(SecurityUserDetailsService securityUserDetailsService) {
        this.securityUserDetailsService = securityUserDetailsService;
    }

    @PostMapping
    public ResponseEntity authenticate(@RequestBody BasicAuthenticationPayload authenticationPayload) {
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/csrf")
    public ResponseEntity getCsrfToken(){
        return ResponseEntity.ok().build();
    }

    @GetMapping("/details")
    public ResponseEntity getUserDetails() {
        final Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) securityUserDetailsService.loadUserByUsername((String) authenticatedUser.getPrincipal());
        return ResponseEntity.ok(user.getUserInfo());
    }

    @GetMapping("/logout")
    @ResponseStatus(code = HttpStatus.OK)
    public void logout() {
    }
}
