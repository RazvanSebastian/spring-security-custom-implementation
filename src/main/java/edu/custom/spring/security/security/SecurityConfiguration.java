package edu.custom.spring.security.security;

import edu.custom.spring.security.security.authentication.BasicAuthenticationFilter;
import edu.custom.spring.security.security.authentication.BasicAuthenticationProvider;
import edu.custom.spring.security.security.authorization.AuthorizationFilter;
import edu.custom.spring.security.security.authorization.StatelessCsrfFilter;
import edu.custom.spring.security.security.jwt.JwtHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

import static edu.custom.spring.security.security.SecurityUtils.JWT_COOKIE_NAME;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final List<String> pathsToSkip;
    private final String logoutPath;
    private final BasicAuthenticationProvider basicAuthenticationProvider;
    private final JwtHandlerService jwtHandlerService;

    @Autowired
    public SecurityConfiguration(
            final @Value("#{'${security.paths.to.skip}'.split(',')}") List<String> pathsToSkip,
            final @Value("${security.paths.logout}") String logoutPath,
            final BasicAuthenticationProvider basicAuthenticationProvider,
            JwtHandlerService jwtHandlerService) {
        this.pathsToSkip = pathsToSkip;
        this.logoutPath = logoutPath;
        this.basicAuthenticationProvider = basicAuthenticationProvider;
        this.jwtHandlerService = jwtHandlerService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] urls = pathsToSkip.toArray(new String[0]);
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(urls).permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/resource/**").hasAnyAuthority("ROLE_ADMIN_WRITE")
                .antMatchers(HttpMethod.GET, "/resource/**").hasAnyAuthority("ROLE_ADMIN_READ", "ROLE_USER_READ")
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // We are using our custom filter instead of default UsernamePasswordAuthenticationFilter implementation
                .addFilterBefore(basicAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new AuthorizationFilter(jwtHandlerService), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new StatelessCsrfFilter(), AuthorizationFilter.class)
                .logout(logoutHandler());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(basicAuthenticationProvider);
    }

    private BasicAuthenticationFilter basicAuthenticationFilter(final AuthenticationManager authenticationManager) {
        final RequestMatcher requestMatcher = new AntPathRequestMatcher("/auth", HttpMethod.POST.toString());
        return new BasicAuthenticationFilter(requestMatcher, authenticationManager, jwtHandlerService);
    }

    private Customizer<LogoutConfigurer<HttpSecurity>> logoutHandler() {
        return (LogoutConfigurer<HttpSecurity> logoutConfigurer) ->
                logoutConfigurer
                        .logoutUrl(logoutPath)
                        .invalidateHttpSession(true)
                        .deleteCookies(JWT_COOKIE_NAME)
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.getWriter().write("Logout successfully!");
                            response.getWriter().flush();
                        });

    }

}
