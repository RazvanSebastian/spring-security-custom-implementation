package edu.custom.spring.security.controller;

import edu.custom.spring.security.model.security.User;
import edu.custom.spring.security.model.security.UserInfo;
import edu.custom.spring.security.security.authentication.credentials.BasicAuthenticationPayload;
import edu.custom.spring.security.service.security.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity authenticate(@RequestBody BasicAuthenticationPayload authenticationPayload) {
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/csrf")
    public ResponseEntity getCsrfToken() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/details")
    public ResponseEntity getUserDetails() {
        User user = userService.getAuthenticatedUser();
        if (Objects.nonNull(user.getUserInfo())) {
            return ResponseEntity.ok(user.getUserInfo());
        } else {
            return ResponseEntity.ok(UserInfo.builder().email(user.getUsername()).build());
        }
    }

    @GetMapping("/logout")
    @ResponseStatus(code = HttpStatus.OK)
    public void logout() {
    }
}
