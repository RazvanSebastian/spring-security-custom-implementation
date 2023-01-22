package edu.custom.spring.security.security;

import edu.custom.spring.security.security.authentication.credentials.BasicAuthenticationProvider;
import edu.custom.spring.security.security.authentication.jwt.JwtAuthenticationProvider;
import edu.custom.spring.security.security.jwt.service.JwtHandlerService;
import edu.custom.spring.security.service.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ProvidersConfiguration {

    private final UserDetailsService userDetailsService;
    private final JwtHandlerService jwtHandlerService;

    public ProvidersConfiguration(UserDetailsService userDetailsService, JwtHandlerService jwtHandlerService) {
        this.userDetailsService = userDetailsService;
        this.jwtHandlerService = jwtHandlerService;
    }

    @Bean
    public BasicAuthenticationProvider basicAuthenticationProvider(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return new BasicAuthenticationProvider(bCryptPasswordEncoder, userDetailsService);
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(jwtHandlerService);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
