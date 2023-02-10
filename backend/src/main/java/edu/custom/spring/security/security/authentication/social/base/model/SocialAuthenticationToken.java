package edu.custom.spring.security.security.authentication.social.base.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
public class SocialAuthenticationToken extends UsernamePasswordAuthenticationToken  {

    private String authorizationCode;

    public SocialAuthenticationToken(String authorizationCode) {
        super(null, null);
        this.authorizationCode = authorizationCode;
    }

    public SocialAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
