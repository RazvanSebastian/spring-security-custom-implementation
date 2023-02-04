package edu.custom.spring.security.security.authentication.social;

import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthUserInfoResponse;
import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthenticationToken;
import edu.custom.spring.security.security.authentication.social.base.service.SocialAuthService;
import edu.custom.spring.security.service.security.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

public class SocialAuthenticationProvider<T extends SocialAuthenticationToken> implements AuthenticationProvider {

    private final SocialAuthService socialAuthService;
    private final UserService userService;
    private final Class<T> supportedAuthClass;


    public SocialAuthenticationProvider(SocialAuthService socialAuthService, UserService userService, Class<T> supportedAuthClass) {
        this.socialAuthService = socialAuthService;
        this.userService = userService;
        this.supportedAuthClass = supportedAuthClass;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (isAuthenticationPayloadValid(authentication)) {
            final SocialAuthenticationToken authenticationToken = (SocialAuthenticationToken) authentication;
            final SocialAuthUserInfoResponse userInfo = socialAuthService.getUserInfo(authenticationToken.getAuthorizationCode());
            final UserDetails userDetails = userService.getOrSave(userInfo, userInfo.getAuthenticationType());
            return new SocialAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
        } else {
            throw new InternalAuthenticationServiceException("Social authentication failed");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(supportedAuthClass);
    }

    private boolean isAuthenticationPayloadValid(Authentication authentication) {
        return authentication instanceof SocialAuthenticationToken
                && StringUtils.hasText(((SocialAuthenticationToken) authentication).getAuthorizationCode());
    }

}
