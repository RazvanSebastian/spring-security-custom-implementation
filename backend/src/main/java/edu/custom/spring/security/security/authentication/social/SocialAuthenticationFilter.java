package edu.custom.spring.security.security.authentication.social;

import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthenticationToken;
import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthenticationType;
import edu.custom.spring.security.security.authentication.social.google.model.GoogleAuthAuthenticationToken;
import edu.custom.spring.security.security.jwt.service.JwtHandlerService;
import edu.custom.spring.security.security.util.CookieUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

public class SocialAuthenticationFilter<T extends SocialAuthenticationToken & SocialAuthenticationType> extends AbstractAuthenticationProcessingFilter {
    private final String AUTHORIZATION_CODE_QUERY_PARAM_NAME = "code";
    private final String REDIRECT_ON_AUTH_SUCCESS = "http://localhost:4200";
    private final String REDIRECT_ON_AUTH_FAILED = "http://localhost:4200/401";

    private final JwtHandlerService jwtHandlerService;
    private final Class<T> supportedAuthClass;

    public SocialAuthenticationFilter(
            RequestMatcher requiresAuthenticationRequestMatcher,
            AuthenticationManager authenticationManager,
            JwtHandlerService jwtHandlerService, Class<T> supportedAuthClass) {
        super(requiresAuthenticationRequestMatcher, authenticationManager);
        this.jwtHandlerService = jwtHandlerService;
        this.supportedAuthClass = supportedAuthClass;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final String authorizationCode = request.getParameter(AUTHORIZATION_CODE_QUERY_PARAM_NAME);
        return getAuthenticationManager().authenticate(getInstanceOfSupportedClass(authorizationCode));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        final String jwtToken = jwtHandlerService.generateAccessToken(authResult);
        CookieUtils.addAccessTokenToCookies(response, jwtToken);
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", REDIRECT_ON_AUTH_SUCCESS);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        CookieUtils.removeAccessTokenFromCookies(response);
        SecurityContextHolder.clearContext();

        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", REDIRECT_ON_AUTH_FAILED);
    }

    private T getInstanceOfSupportedClass(String authorizationCode) {
        try {
            return supportedAuthClass.getConstructor(String.class).newInstance(authorizationCode);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException("Internal server error");
        }
    }
}
