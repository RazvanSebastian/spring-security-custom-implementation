package edu.custom.spring.security.security.authentication.jwt;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SkipRequestMatcher implements RequestMatcher {

    private RequestMatcher wrappedRequestMatcher;

    public SkipRequestMatcher(Set<String> allowedPaths) {
        this.setSkipRequestMatcher(allowedPaths);
    }

    public SkipRequestMatcher(List<RequestMatcher> requestMatchers){
        this.setSkipRequestMatcher(requestMatchers);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return wrappedRequestMatcher.matches(request);
    }

    private void setSkipRequestMatcher(List<RequestMatcher> allowedRequestMatchers) {
        this.wrappedRequestMatcher = new NegatedRequestMatcher(new OrRequestMatcher(allowedRequestMatchers));
    }

    private void setSkipRequestMatcher(Set<String> allowedPaths) {
        final List<RequestMatcher> pathRequestMatchers = allowedPaths.stream()
                .map(path -> new AntPathRequestMatcher(path))
                .collect(Collectors.toList());
        final OrRequestMatcher orRequestMatcher = new OrRequestMatcher(pathRequestMatchers);
        this.wrappedRequestMatcher = new NegatedRequestMatcher(orRequestMatcher);
    }
}
