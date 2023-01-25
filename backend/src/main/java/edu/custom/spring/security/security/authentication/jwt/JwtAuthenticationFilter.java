package edu.custom.spring.security.security.authentication.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.custom.spring.security.model.exception.ErrorResponse;
import edu.custom.spring.security.security.util.SecurityUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static edu.custom.spring.security.security.util.SecurityUtils.JWT_COOKIE_NAME;

/**
 * This filter search for the cookie which hold the access token and if that cookie was found,
 * verify and decode the JWT, extract the claims and build an {@link Authentication} object
 * which is passed to the {@link org.springframework.security.core.context.SecurityContext}.
 */
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(
            SkipRequestMatcher skipRequestMatcher,
            AuthenticationManager authenticationManager) {
        super(skipRequestMatcher, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final Optional<Cookie> accessToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[]{}))
                .filter(cookie -> cookie.getName().equals(JWT_COOKIE_NAME))
                .findFirst();
        if (accessToken.isPresent()) {
            final String untrustedJwtToken = accessToken.get().getValue();
            return getAuthenticationManager().authenticate(new JwtTokenAuthentication(untrustedJwtToken));
        } else {
            throw new BadCredentialsException("Access denied. Provide access token.");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authResult);
        SecurityContextHolder.setContext(securityContext);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityUtils.removeAccessTokenFromCookies(response);
        SecurityContextHolder.clearContext();
        final ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED, failed.getMessage());
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
