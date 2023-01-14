package edu.custom.spring.security.security.authorization;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SkipRequestMatcher implements RequestMatcher {

    private final Collection<String> allowedPaths;

    public SkipRequestMatcher(Collection<String> allowedPaths) {
        this.allowedPaths = allowedPaths;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        final List<RequestMatcher> pathRequestMatchers = allowedPaths.stream()
                .map(path -> new AntPathRequestMatcher(path))
                .collect(Collectors.toList());
        final OrRequestMatcher orRequestMatcher = new OrRequestMatcher(pathRequestMatchers);
        NegatedRequestMatcher matcher = new NegatedRequestMatcher(orRequestMatcher);
        return matcher.matches(request);
    }
}
