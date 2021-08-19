package edu.custom.spring.security.security;

import edu.custom.spring.security.security.authentication.BasicAuthenticationProvider;
import edu.custom.spring.security.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ProvidersConfiguration {

    private final UserDetailsService userDetailsService;

    @Autowired
    public ProvidersConfiguration(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public BasicAuthenticationProvider basicAuthenticationProvider(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return new BasicAuthenticationProvider(bCryptPasswordEncoder, userDetailsService);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
