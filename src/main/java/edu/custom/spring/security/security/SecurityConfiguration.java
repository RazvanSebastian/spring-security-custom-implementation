package edu.custom.spring.security.security;

import edu.custom.spring.security.security.authentication.BasicAuthenticationFilter;
import edu.custom.spring.security.security.authentication.BasicAuthenticationProvider;
import edu.custom.spring.security.security.authorization.AuthorizationFilter;
import edu.custom.spring.security.security.jwt.JwtHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final List<String> pathsToSkip;
    private final BasicAuthenticationProvider basicAuthenticationProvider;
    private final JwtHandlerService jwtHandlerService;

    @Autowired
    public SecurityConfiguration(
            final BasicAuthenticationProvider basicAuthenticationProvider,
            final @Value("#{'${security.paths.to.skip}'.split(',')}") List<String> pathsToSkip,
            JwtHandlerService jwtHandlerService) {
        this.basicAuthenticationProvider = basicAuthenticationProvider;
        this.pathsToSkip = pathsToSkip;
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
                .antMatchers(HttpMethod.GET, "/auth/details").authenticated()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new AuthorizationFilter(authenticationManager(), jwtHandlerService))
                // We are using our custom filter instead of default UsernamePasswordAuthenticationFilter implementation
                .addFilterBefore(basicAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(basicAuthenticationProvider);
    }

    private BasicAuthenticationFilter basicAuthenticationFilter(final AuthenticationManager authenticationManager) {
        final RequestMatcher requestMatcher = new AntPathRequestMatcher("/auth", HttpMethod.POST.toString());
        return new BasicAuthenticationFilter(requestMatcher, authenticationManager, jwtHandlerService);
    }

}
