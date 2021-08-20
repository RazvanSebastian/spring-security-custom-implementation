package edu.custom.spring.security.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.custom.spring.security.security.CookiesUtils;
import edu.custom.spring.security.security.jwt.JwtHandlerService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BasicAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final JwtHandlerService jwtHandlerService;

    public BasicAuthenticationFilter(
            RequestMatcher requiresAuthenticationRequestMatcher,
            AuthenticationManager authenticationManager,
            JwtHandlerService jwtHandlerService) {
        super(requiresAuthenticationRequestMatcher, authenticationManager);
        this.jwtHandlerService = jwtHandlerService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        final UsernamePasswordAuthenticationToken authentication = extractUserCredentials(httpServletRequest);
        return getAuthenticationManager().authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        Cookie cookie = CookiesUtils.createAccessTokenCookie(jwtHandlerService.generateAccessToken(authResult), false);
        response.addCookie(cookie);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }

    private UsernamePasswordAuthenticationToken extractUserCredentials(final HttpServletRequest request) throws IOException {
        BasicAuthenticationPayload authenticationPayload = OBJECT_MAPPER.readValue(request.getInputStream(), BasicAuthenticationPayload.class);
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                authenticationPayload.getPrincipal(),
                authenticationPayload.getCredentials()
        );
        return usernamePasswordAuthenticationToken;
    }

}
