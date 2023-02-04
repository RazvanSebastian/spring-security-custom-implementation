package edu.custom.spring.security.security.authentication.social.github.controller;

import edu.custom.spring.security.security.authentication.social.github.service.GithubAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/github-auth")
public class GithubAuthController {

    private final GithubAuthService githubAuthService;

    public GithubAuthController(GithubAuthService githubAuthService) {
        this.githubAuthService = githubAuthService;
    }

    /**
     * Returns the uri to Google authentication and consent page.
     *
     * @return
     */
    @GetMapping("/consent")
    public ResponseEntity getGoogleAuthUrl() {
        return ResponseEntity.ok(githubAuthService.getConsentAuthUri());
    }
}
