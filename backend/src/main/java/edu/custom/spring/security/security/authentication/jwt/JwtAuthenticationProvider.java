package edu.custom.spring.security.security.authentication.jwt;

import edu.custom.spring.security.security.jwt.models.JwtClaims;
import edu.custom.spring.security.security.jwt.service.JwtHandlerService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationProvider implements AuthenticationProvider {
    private static final String ACCESS_TOKEN_EXPIRED_MSG = "Access token expired.";
    private static final String ACCESS_TOKEN_UNSUPPORTED_MSG = "Unsupported jwt token.";
    private static final String ACCESS_TOKEN_MALFORMED_MSG = "Malformed jwt token.";

    private final JwtHandlerService jwtHandlerService;

    public JwtAuthenticationProvider(JwtHandlerService jwtHandlerService) {
        this.jwtHandlerService = jwtHandlerService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof JwtTokenAuthentication) {
            try {
                final String untrustedJwtToken = ((JwtTokenAuthentication) authentication).getJwtAccessToken();
                final JwtClaims jwtClaims = jwtHandlerService.decodeAccessToken(untrustedJwtToken);
                return jwtClaims.getAuthentication();
            } catch (ExpiredJwtException e) {
                throw new BadCredentialsException(ACCESS_TOKEN_EXPIRED_MSG);
            } catch (UnsupportedJwtException e) {
                throw new BadCredentialsException(ACCESS_TOKEN_UNSUPPORTED_MSG);
            } catch (MalformedJwtException jwtException) {
                throw new BadCredentialsException(ACCESS_TOKEN_MALFORMED_MSG);
            }
        } else {
            throw new InternalAuthenticationServiceException("Authentication failed");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtTokenAuthentication.class);
    }
}
