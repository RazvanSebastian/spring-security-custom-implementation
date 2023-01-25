package edu.custom.spring.security.security.authentication.social.google;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.custom.spring.security.security.jwt.service.JwtHandlerService;
import edu.custom.spring.security.security.util.SecurityUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GoogleAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final String AUTHORIZATION_CODE_QUERY_PARAM_NAME = "code";
    private static final ObjectMapper objectMapper = new ObjectMapper();


    private final JwtHandlerService jwtHandlerService;

    public GoogleAuthenticationFilter(
            RequestMatcher requiresAuthenticationRequestMatcher,
            AuthenticationManager authenticationManager,
            JwtHandlerService jwtHandlerService) {
        super(requiresAuthenticationRequestMatcher, authenticationManager);
        this.jwtHandlerService = jwtHandlerService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        final String authorizationCode = request.getParameter(AUTHORIZATION_CODE_QUERY_PARAM_NAME);
        final GoogleAuthenticationToken authentication = new GoogleAuthenticationToken(authorizationCode);
        return getAuthenticationManager().authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        final String jwtToken = jwtHandlerService.generateAccessToken(authResult);
        SecurityUtils.addAccessTokenToCookies(response, jwtToken);
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", "http://localhost:4200");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityUtils.removeAccessTokenFromCookies(response);
        SecurityContextHolder.clearContext();

        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", "http://localhost:4200/401");
    }
}
