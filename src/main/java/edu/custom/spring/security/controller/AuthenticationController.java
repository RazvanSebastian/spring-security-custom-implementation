package edu.custom.spring.security.controller;

import edu.custom.spring.security.security.authentication.BasicAuthenticationPayload;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @PostMapping
    public ResponseEntity authenticate(@RequestBody BasicAuthenticationPayload authenticationPayload) {
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/details")
    public ResponseEntity getUserDetails() {
        final Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(authenticatedUser);
    }
}
