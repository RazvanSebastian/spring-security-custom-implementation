package edu.custom.spring.security.security.authentication.social.google.model;

import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class GoogleAuthAuthenticationToken extends SocialAuthenticationToken {

    public GoogleAuthAuthenticationToken(String authorizationCode) {
        super(authorizationCode);
    }

    public GoogleAuthAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

}
