package edu.custom.spring.security.security.authentication.social.github;

import edu.custom.spring.security.security.authentication.social.github.model.GithubUserInfoResponse;
import edu.custom.spring.security.security.authentication.social.github.service.GithubAuthService;
import edu.custom.spring.security.security.authentication.social.google.model.GoogleUserInfoResponse;
import edu.custom.spring.security.security.authentication.social.google.service.GoogleAuthService;
import edu.custom.spring.security.service.security.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

public class GithubAuthenticationProvider implements AuthenticationProvider {

    private final GithubAuthService githubAuthService;
    private final UserService userService;

    public GithubAuthenticationProvider(GithubAuthService githubAuthService, UserService userService) {
        this.githubAuthService = githubAuthService;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (isAuthenticationPayloadValid(authentication)) {
            final GithubAuthenticationToken authenticationToken = (GithubAuthenticationToken) authentication;
            final GithubUserInfoResponse userInfo = githubAuthService.getUserInfo(authenticationToken.getAuthorizationCode());
            final UserDetails userDetails = userService.getOrSaveNewUserBySocialAuthentication(userInfo);
            return new GithubAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
        } else {
            throw new InternalAuthenticationServiceException("Authentication with google failed");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(GithubAuthenticationToken.class);
    }

    private boolean isAuthenticationPayloadValid(Authentication authentication) {
        return authentication instanceof GithubAuthenticationToken
                && StringUtils.hasText(((GithubAuthenticationToken) authentication).getAuthorizationCode());
    }
}
