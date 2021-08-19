package edu.custom.spring.security.security.authorization;

import edu.custom.spring.security.security.CookiesUtils;
import edu.custom.spring.security.security.jwt.JwtHandlerService;
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

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtHandlerService jwtHandlerService;

    public AuthorizationFilter(final AuthenticationManager authenticationManager, final JwtHandlerService jwtHandlerService) {
        super(authenticationManager);
        this.jwtHandlerService = jwtHandlerService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        final Optional<Cookie> accessToken = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(CookiesUtils.getJwtCookieName()))
                .findFirst();
        if (!accessToken.isPresent()) {
            chain.doFilter(request, response);
            return;
        }
        final Authentication authentication = jwtHandlerService.decodeAccessToken(accessToken.get().getValue());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

}
