package edu.custom.spring.security.security.authentication.social.github.model;

import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class GithubAuthAuthenticationToken extends SocialAuthenticationToken {

    public GithubAuthAuthenticationToken(String authorizationCode) {
        super(authorizationCode);
    }

    public GithubAuthAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

}
