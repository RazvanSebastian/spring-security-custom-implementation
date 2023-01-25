package edu.custom.spring.security.security.authentication.social.google.controller;

import edu.custom.spring.security.security.authentication.social.google.service.GoogleAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/google-auth")
public class GoogleAuthController {

    private final GoogleAuthService googleAuthService;

    public GoogleAuthController(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;
    }

    @GetMapping("/consent")
    public ResponseEntity getGoogleAuthUrl() {
        return ResponseEntity.ok(googleAuthService.getGoogleAuthConsentRequestUri());
    }

}
