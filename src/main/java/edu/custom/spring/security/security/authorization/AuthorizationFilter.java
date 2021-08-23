package edu.custom.spring.security.security.authorization;

import edu.custom.spring.security.security.SecurityUtils;
import edu.custom.spring.security.security.authorization.handler.CustomAccessDeniedException;
import edu.custom.spring.security.security.authorization.handler.CustomAccessDeniedHandler;
import edu.custom.spring.security.security.jwt.JwtClaims;
import edu.custom.spring.security.security.jwt.JwtHandlerService;
import io.jsonwebtoken.JwtException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static edu.custom.spring.security.security.SecurityUtils.CSRF_JWT_CLAIM_HEADER_NAME;
import static edu.custom.spring.security.security.SecurityUtils.JWT_COOKIE_NAME;

/**
 * This filter search for the cookie which hold the access token and if that cookie is found,
 * verify and decode the JWT, extract the claims and build an {@link Authentication} object
 * which is passed to the {@link org.springframework.security.core.context.SecurityContext}.
 */
@Order(0)
public class AuthorizationFilter extends OncePerRequestFilter {

    private final JwtHandlerService jwtHandlerService;
    private AccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();

    public AuthorizationFilter(final JwtHandlerService jwtHandlerService) {
        this.jwtHandlerService = jwtHandlerService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            final Optional<Cookie> accessToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[]{}))
                    .filter(cookie -> cookie.getName().equals(JWT_COOKIE_NAME))
                    .findFirst();
            if (!accessToken.isPresent()) {
                SecurityContextHolder.clearContext();
                accessDeniedHandler.handle(request, response, new CustomAccessDeniedException("Access token is missing.", HttpStatus.UNAUTHORIZED));
                return;
            }
            final JwtClaims jwtClaims = jwtHandlerService.decodeAccessToken(accessToken.get().getValue());
            SecurityContextHolder.getContext().setAuthentication(jwtClaims.getAuthentication());
            /**
             * Add csrf token from JWT claims into the response header in order to be verified by {@link StatelessCsrfFilter}.
             * Tis csrf jwt claim is added into response because request can not be modified.
             */
            response.addHeader(CSRF_JWT_CLAIM_HEADER_NAME, jwtClaims.getCsrfToken());
            chain.doFilter(request, response);
        } catch (JwtException jwtException) {
            SecurityContextHolder.clearContext();
            accessDeniedHandler.handle(request, response, new CustomAccessDeniedException(jwtException.getMessage(), HttpStatus.UNAUTHORIZED));
        }
    }

}
