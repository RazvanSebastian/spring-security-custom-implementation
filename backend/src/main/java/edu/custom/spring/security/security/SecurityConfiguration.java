package edu.custom.spring.security.security;

import edu.custom.spring.security.security.authentication.credentials.BasicAuthenticationFilter;
import edu.custom.spring.security.security.authentication.credentials.BasicAuthenticationProvider;
import edu.custom.spring.security.security.authentication.jwt.JwtAuthenticationFilter;
import edu.custom.spring.security.security.authentication.jwt.JwtAuthenticationProvider;
import edu.custom.spring.security.security.authentication.jwt.SkipRequestMatcher;
import edu.custom.spring.security.security.authentication.social.SocialAuthenticationFilter;
import edu.custom.spring.security.security.authentication.social.SocialAuthenticationProvider;
import edu.custom.spring.security.security.authentication.social.github.model.GithubAuthAuthenticationToken;
import edu.custom.spring.security.security.authentication.social.google.model.GoogleAuthAuthenticationToken;
import edu.custom.spring.security.security.csrf.CustomCsrfFilterWrapper;
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
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Set;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final Set<String> authPathsToSkip;
    private final String logoutPath;
    private final BasicAuthenticationProvider basicAuthenticationProvider;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final SocialAuthenticationProvider githubAuthenticationProvider;
    private final SocialAuthenticationProvider googleAuthenticationProvider;
    private final JwtHandlerService jwtHandlerService;
    private final CustomCsrfFilterWrapper customCsrfFilterWrapper;

    @Autowired
    public SecurityConfiguration(
            final @Value("#{'${security.paths.auth.to.skip}'.split(',')}") Set<String> authPathsToSkip,
            final @Value("${security.paths.logout}") String logoutPath,
            final BasicAuthenticationProvider basicAuthenticationProvider,
            final JwtAuthenticationProvider jwtAuthenticationProvider,
            final SocialAuthenticationProvider githubAuthenticationProvider,
            final SocialAuthenticationProvider googleAuthenticationProvider,
            final JwtHandlerService jwtHandlerService,
            final CustomCsrfFilterWrapper customCsrfFilterWrapper) {
        this.authPathsToSkip = authPathsToSkip;
        this.logoutPath = logoutPath;
        this.basicAuthenticationProvider = basicAuthenticationProvider;
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.githubAuthenticationProvider = githubAuthenticationProvider;
        this.googleAuthenticationProvider = googleAuthenticationProvider;
        this.jwtHandlerService = jwtHandlerService;
        this.customCsrfFilterWrapper = customCsrfFilterWrapper;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final String[] allowedUrls = authPathsToSkip.toArray(new String[0]);
        http.cors()
                .and().headers().frameOptions().disable() // Only for h2 testing
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
                .addFilterBefore(customCsrfFilterWrapper.getInstance(), CsrfFilter.class)
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

    private SocialAuthenticationFilter githubAuthenticationFilter(final AuthenticationManager authenticationManager) {
        final RequestMatcher requestMatcher = new AntPathRequestMatcher("/github-auth/consent/callback", HttpMethod.GET.toString());
        return new SocialAuthenticationFilter(requestMatcher, authenticationManager, jwtHandlerService, GithubAuthAuthenticationToken.class);
    }

    private SocialAuthenticationFilter googleAuthenticationFilter(final AuthenticationManager authenticationManager) {
        final RequestMatcher requestMatcher = new AntPathRequestMatcher("/google-auth/consent/callback", HttpMethod.GET.toString());
        return new SocialAuthenticationFilter(requestMatcher, authenticationManager, jwtHandlerService, GoogleAuthAuthenticationToken.class);
    }

    private JwtAuthenticationFilter jwtAuthenticationFilter(final AuthenticationManager authenticationManager) {
        final SkipRequestMatcher skipRequestMatcher = new SkipRequestMatcher(authPathsToSkip);
        return new JwtAuthenticationFilter(skipRequestMatcher, authenticationManager);
    }

    private Customizer<LogoutConfigurer<HttpSecurity>> logoutConfigure() {
        return (LogoutConfigurer<HttpSecurity> logoutConfigure) ->
                logoutConfigure
                        .logoutUrl(logoutPath)
                        .logoutSuccessHandler((request, response, authentication) -> {
                        })
                        .invalidateHttpSession(true)
                        .addLogoutHandler((request, response, authentication) -> CookieUtils.removeAccessTokenFromCookies(response));
    }
}
