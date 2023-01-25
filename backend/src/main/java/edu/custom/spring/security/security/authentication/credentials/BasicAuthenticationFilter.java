package edu.custom.spring.security.security.authentication.credentials;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.custom.spring.security.model.exception.ErrorResponse;
import edu.custom.spring.security.security.util.SecurityUtils;
import edu.custom.spring.security.security.jwt.service.JwtHandlerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

public class BasicAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtHandlerService jwtHandlerService;

    public BasicAuthenticationFilter(
            RequestMatcher requiresAuthenticationRequestMatcher,
            AuthenticationManager authenticationManager,
            JwtHandlerService jwtHandlerService) {
        super(requiresAuthenticationRequestMatcher, authenticationManager);
        this.jwtHandlerService = jwtHandlerService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException {
        final UsernamePasswordAuthenticationToken authentication = extractUserCredentials(httpServletRequest);
        return getAuthenticationManager().authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws ServletException, IOException {
        final String jwtToken = jwtHandlerService.generateAccessToken(authResult);
        SecurityUtils.addAccessTokenToCookies(response, jwtToken);
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

    private UsernamePasswordAuthenticationToken extractUserCredentials(final HttpServletRequest request) throws IOException {
        BasicAuthenticationPayload authenticationPayload = objectMapper.readValue(request.getInputStream(), BasicAuthenticationPayload.class);
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                authenticationPayload.getPrincipal(),
                authenticationPayload.getCredentials()
        );
        return usernamePasswordAuthenticationToken;
    }

}
