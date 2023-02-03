package edu.custom.spring.security.security.authentication.social.github;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class GithubAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private String authorizationCode;

    public GithubAuthenticationToken(String authorizationCode){
        super(null, null);
        this.authorizationCode = authorizationCode;
    }

    public GithubAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }
}
