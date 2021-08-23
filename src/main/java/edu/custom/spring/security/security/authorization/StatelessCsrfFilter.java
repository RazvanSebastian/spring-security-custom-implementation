package edu.custom.spring.security.security.authorization;

import edu.custom.spring.security.security.authorization.handler.CustomAccessDeniedException;
import edu.custom.spring.security.security.authorization.handler.CustomAccessDeniedHandler;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import static edu.custom.spring.security.security.SecurityUtils.CSRF_HEADER_NAME;
import static edu.custom.spring.security.security.SecurityUtils.CSRF_JWT_CLAIM_HEADER_NAME;
import static org.apache.logging.log4j.util.Strings.isEmpty;

public class StatelessCsrfFilter extends OncePerRequestFilter {

    private final RequestMatcher requestMatcher = new DefaultRequiresCsrfMatcher();
    private AccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (requestMatcher.matches(httpServletRequest)) {
            // CSRF token from browser header
            final String csrfFromHeader = httpServletRequest.getHeader(CSRF_HEADER_NAME);
            // CSRF token from JWT extracted in AuthorizationFilter
            final String csrfFromAccessToken = httpServletResponse.getHeader(CSRF_JWT_CLAIM_HEADER_NAME);
            if (isEmpty(csrfFromHeader) || isEmpty(csrfFromAccessToken) || !csrfFromHeader.equals(csrfFromAccessToken)) {
                httpServletResponse.setHeader(CSRF_JWT_CLAIM_HEADER_NAME, null);
                SecurityContextHolder.clearContext();
                accessDeniedHandler.handle(
                        httpServletRequest,
                        httpServletResponse,
                        new CustomAccessDeniedException("Missing or non-matching CSRF-token.", HttpStatus.FORBIDDEN));
                return;
            }
        }
        httpServletResponse.setHeader(CSRF_JWT_CLAIM_HEADER_NAME, null);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private static final class DefaultRequiresCsrfMatcher implements RequestMatcher {

        private final HashSet<String> allowedMethods = new HashSet<>(Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS"));

        @Override
        public boolean matches(HttpServletRequest request) {
            return !this.allowedMethods.contains(request.getMethod());
        }

        @Override
        public String toString() {
            return "CsrfNotRequired " + this.allowedMethods;
        }

    }
}
