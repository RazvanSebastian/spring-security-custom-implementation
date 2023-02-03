package edu.custom.spring.security.security;

import edu.custom.spring.security.security.authentication.credentials.BasicAuthenticationFilter;
import edu.custom.spring.security.security.authentication.credentials.BasicAuthenticationProvider;
import edu.custom.spring.security.security.authentication.jwt.JwtAuthenticationFilter;
import edu.custom.spring.security.security.authentication.jwt.JwtAuthenticationProvider;
import edu.custom.spring.security.security.authentication.jwt.SkipRequestMatcher;
import edu.custom.spring.security.security.authentication.social.github.GithubAuthenticationFilter;
import edu.custom.spring.security.security.authentication.social.github.GithubAuthenticationProvider;
import edu.custom.spring.security.security.authentication.social.google.GoogleAuthenticationFilter;
import edu.custom.spring.security.security.authentication.social.google.GoogleAuthenticationProvider;
import edu.custom.spring.security.security.handler.CustomAccessDeniedHandler;
import edu.custom.spring.security.security.jwt.service.JwtHandlerService;
import edu.custom.spring.security.security.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.*;

import java.util.Arrays;
import java.util.Set;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final Set<String> pathsToSkip;
    private final String logoutPath;
    private final BasicAuthenticationProvider basicAuthenticationProvider;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final GithubAuthenticationProvider githubAuthenticationProvider;
    private final GoogleAuthenticationProvider googleAuthenticationProvider;
    private final JwtHandlerService jwtHandlerService;

    @Autowired
    public SecurityConfiguration(
            final @Value("#{'${security.paths.to.skip}'.split(',')}") Set<String> pathsToSkip,
            final @Value("${security.paths.logout}") String logoutPath,
            final BasicAuthenticationProvider basicAuthenticationProvider,
            JwtAuthenticationProvider jwtAuthenticationProvider,
            GithubAuthenticationProvider githubAuthenticationProvider, GoogleAuthenticationProvider googleAuthenticationProvider,
            JwtHandlerService jwtHandlerService) {
        this.pathsToSkip = pathsToSkip;
        this.logoutPath = logoutPath;
        this.basicAuthenticationProvider = basicAuthenticationProvider;
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.githubAuthenticationProvider = githubAuthenticationProvider;
        this.googleAuthenticationProvider = googleAuthenticationProvider;
        this.jwtHandlerService = jwtHandlerService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final String[] allowedUrls = pathsToSkip.toArray(new String[0]);
        http.cors()
                .and().exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .and().csrf().disable()
                .authorizeRequests()
                .antMatchers(allowedUrls).permitAll()
                .and().authorizeRequests()
                .anyRequest().authenticated()
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(customCsrfFilter(), CsrfFilter.class)
                .addFilterBefore(basicAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(githubAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(googleAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .logout(logoutConfigure());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(basicAuthenticationProvider);
        auth.authenticationProvider(jwtAuthenticationProvider);
        auth.authenticationProvider(googleAuthenticationProvider);
        auth.authenticationProvider(githubAuthenticationProvider);
    }

    private BasicAuthenticationFilter basicAuthenticationFilter(final AuthenticationManager authenticationManager) {
        final RequestMatcher requestMatcher = new AntPathRequestMatcher("/auth", HttpMethod.POST.toString());
        return new BasicAuthenticationFilter(requestMatcher, authenticationManager, jwtHandlerService);
    }

    private GithubAuthenticationFilter githubAuthenticationFilter(final AuthenticationManager authenticationManager){
        final RequestMatcher requestMatcher = new AntPathRequestMatcher("/github-auth/consent/callback", HttpMethod.GET.toString());
        return new GithubAuthenticationFilter(requestMatcher, authenticationManager, jwtHandlerService);
    }

    private GoogleAuthenticationFilter googleAuthenticationFilter(final AuthenticationManager authenticationManager) {
        final RequestMatcher requestMatcher = new AntPathRequestMatcher("/google-auth/consent/callback", HttpMethod.GET.toString());
        return new GoogleAuthenticationFilter(requestMatcher, authenticationManager, jwtHandlerService);
    }

    private JwtAuthenticationFilter jwtAuthenticationFilter(final AuthenticationManager authenticationManager) {
        final SkipRequestMatcher skipRequestMatcher = new SkipRequestMatcher(pathsToSkip);
        return new JwtAuthenticationFilter(skipRequestMatcher, authenticationManager);
    }

    private Customizer<LogoutConfigurer<HttpSecurity>> logoutConfigure() {
        return (LogoutConfigurer<HttpSecurity> logoutConfigure) ->
                logoutConfigure
                        .logoutUrl(logoutPath)
                        .logoutSuccessHandler((request, response, authentication) -> {})
                        .invalidateHttpSession(true)
                        .addLogoutHandler((request, response, authentication) -> CookieUtils.removeAccessTokenFromCookies(response));
    }

    private CsrfFilter customCsrfFilter() {
        RequestMatcher skipRequestMatcher = new SkipRequestMatcher(
                Arrays.asList(
                        new RequestHeaderRequestMatcher(
                                "Referer",
                                "http://localhost:8080/api/swagger-ui/index.html"
                        ),
                        new AntPathRequestMatcher("/swagger-ui/**"),
                        new AntPathRequestMatcher("/google-auth/consent/callback"),
                        new AntPathRequestMatcher("/github-auth/consent/callback")
        ));
        CsrfFilter csrfFilter = new CsrfFilter(csrfTokenRepository());
        csrfFilter.setAccessDeniedHandler(new CustomAccessDeniedHandler());
        csrfFilter.setRequireCsrfProtectionMatcher(skipRequestMatcher);
        return csrfFilter;
    }

    private CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository csrfTokenRepository = new CookieCsrfTokenRepository();
        csrfTokenRepository.setCookieHttpOnly(false);
        csrfTokenRepository.setCookiePath("/");
        return csrfTokenRepository;
    }

}
