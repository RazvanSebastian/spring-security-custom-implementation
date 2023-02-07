package edu.custom.spring.security.security.csrf;

import edu.custom.spring.security.security.authentication.jwt.SkipRequestMatcher;
import edu.custom.spring.security.security.handler.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CustomCsrfFilterWrapper {

    private CsrfFilter csrfFilter;
    private final Set<String> csrfPathsToSkip;

    public CustomCsrfFilterWrapper(final @Value("#{'${security.paths.csrf.to.skip}'.split(',')}") Set<String> csrfPathsToSkip) {
        this.csrfPathsToSkip = csrfPathsToSkip;
        initializeCustomCsrfFilter();
    }

    public CsrfFilter getInstance() {
        return this.csrfFilter;
    }

    private void initializeCustomCsrfFilter() {
        CsrfFilter csrfFilter = new CsrfFilter(csrfTokenRepository());
        csrfFilter.setAccessDeniedHandler(new CustomAccessDeniedHandler());
        csrfFilter.setRequireCsrfProtectionMatcher(skipRequestMatcher());
        this.csrfFilter = csrfFilter;
    }

    private SkipRequestMatcher skipRequestMatcher() {
        List<RequestMatcher> requestMatchers = csrfPathsToSkip.stream()
                .map(pathToSkip -> new AntPathRequestMatcher(pathToSkip))
                .collect(Collectors.toList());
        requestMatchers.add(new RequestHeaderRequestMatcher(
                "Referer",
                "http://localhost:8080/api/swagger-ui/index.html")
        );
        return new SkipRequestMatcher(requestMatchers);
    }

    private CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository csrfTokenRepository = new CookieCsrfTokenRepository();
        csrfTokenRepository.setCookieHttpOnly(false);
        csrfTokenRepository.setCookiePath("/");
        return csrfTokenRepository;
    }
}
