package edu.custom.spring.security.security.authentication.social.google;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class GoogleAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String authorizationCode;

    public GoogleAuthenticationToken(String authorizationCode){
        super(null, null);
        this.authorizationCode = authorizationCode;
    }

    public GoogleAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }
}
