package edu.custom.spring.security.security.authentication.social.google;

import edu.custom.spring.security.security.authentication.social.google.model.GoogleUserInfoResponse;
import edu.custom.spring.security.security.authentication.social.google.service.GoogleAuthService;
import edu.custom.spring.security.security.service.SecurityUserDetailsService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

public class GoogleAuthenticationProvider implements AuthenticationProvider {

    private final GoogleAuthService googleAuthService;
    private final SecurityUserDetailsService userDetailsService;

    public GoogleAuthenticationProvider(GoogleAuthService googleAuthService, SecurityUserDetailsService userDetailsService) {
        this.googleAuthService = googleAuthService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (isAuthenticationPayloadValid(authentication)) {
            final GoogleAuthenticationToken authenticationToken = (GoogleAuthenticationToken) authentication;
            final GoogleUserInfoResponse googleUserInfoResponse = googleAuthService.getUserInfo(authenticationToken.getAuthorizationCode());
            final UserDetails userDetails = userDetailsService.retrieveOrRegisterNewUserWithSocialAuth(googleUserInfoResponse);
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
