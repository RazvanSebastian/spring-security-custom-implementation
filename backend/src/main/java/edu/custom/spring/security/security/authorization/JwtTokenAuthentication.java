package edu.custom.spring.security.security.authorization;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

/**
 * Used to pass it to {@link JwtAuthenticationProvider} for validation.
 */
@Getter
public class JwtTokenAuthentication extends AbstractAuthenticationToken {

    private final String jwtAccessToken;

    public JwtTokenAuthentication(String jwtAccessToken) {
        super(Collections.emptyList());
        this.jwtAccessToken = jwtAccessToken;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
