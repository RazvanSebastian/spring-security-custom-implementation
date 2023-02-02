package edu.custom.spring.security.security.authentication.social.google;

import edu.custom.spring.security.security.authentication.social.google.model.GoogleUserInfoResponse;
import edu.custom.spring.security.security.authentication.social.google.service.GoogleAuthService;
import edu.custom.spring.security.service.security.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

public class GoogleAuthenticationProvider implements AuthenticationProvider {

    private final GoogleAuthService googleAuthService;
    private final UserService userService;

    public GoogleAuthenticationProvider(GoogleAuthService googleAuthService, UserService userService) {
        this.googleAuthService = googleAuthService;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (isAuthenticationPayloadValid(authentication)) {
            final GoogleAuthenticationToken authenticationToken = (GoogleAuthenticationToken) authentication;
            final GoogleUserInfoResponse googleUserInfoResponse = googleAuthService.getUserInfo(authenticationToken.getAuthorizationCode());
            final UserDetails userDetails = userService.getOrSaveNewUserBySocialAuthentication(googleUserInfoResponse);
            return new GoogleAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
        } else {
            throw new InternalAuthenticationServiceException("Authentication with google failed");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(GoogleAuthenticationToken.class);
    }

    private boolean isAuthenticationPayloadValid(Authentication authentication) {
        return authentication instanceof GoogleAuthenticationToken
                && StringUtils.hasText(((GoogleAuthenticationToken) authentication).getAuthorizationCode());
    }
}
