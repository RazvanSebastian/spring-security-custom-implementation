package edu.custom.spring.security.security.authorization;

import edu.custom.spring.security.security.CookiesUtils;
import edu.custom.spring.security.security.jwt.JwtHandlerService;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * This filter search for the cookie which hold the access token and if that cookie is found,
 * verify and decode the JWT, extract the claims and build an {@link Authentication} object
 * which is passed to the {@link org.springframework.security.core.context.SecurityContext}.
 */
public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtHandlerService jwtHandlerService;

    public AuthorizationFilter(final AuthenticationManager authenticationManager, final JwtHandlerService jwtHandlerService) {
        super(authenticationManager);
        this.jwtHandlerService = jwtHandlerService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            final Optional<Cookie> accessToken = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[]{}))
                    .filter(cookie -> cookie.getName().equals(CookiesUtils.getJwtCookieName()))
                    .findFirst();
            if (!accessToken.isPresent()) {
                chain.doFilter(request, response);
                return;
            }
            final Authentication authentication = jwtHandlerService.decodeAccessToken(accessToken.get().getValue());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (JwtException jwtException) {
            response.addCookie(CookiesUtils.createAccessTokenCookie(null, true));
            chain.doFilter(request, response);
        }
    }

}
