package edu.custom.spring.security.security;

import edu.custom.spring.security.security.authentication.credentials.BasicAuthenticationProvider;
import edu.custom.spring.security.security.authentication.jwt.JwtAuthenticationProvider;
import edu.custom.spring.security.security.authentication.social.SocialAuthenticationProvider;
import edu.custom.spring.security.security.authentication.social.github.model.GithubAuthAuthenticationToken;
import edu.custom.spring.security.security.authentication.social.github.service.GithubAuthService;
import edu.custom.spring.security.security.authentication.social.google.model.GoogleAuthAuthenticationToken;
import edu.custom.spring.security.security.authentication.social.google.service.GoogleAuthService;
import edu.custom.spring.security.security.jwt.service.JwtHandlerService;
import edu.custom.spring.security.service.security.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ProvidersConfiguration {

    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final JwtHandlerService jwtHandlerService;
    private final GithubAuthService githubAuthService;
    private final GoogleAuthService googleAuthService;

    public ProvidersConfiguration(UserService userService, UserDetailsService userDetailsService, JwtHandlerService jwtHandlerService, GithubAuthService githubAuthService, GoogleAuthService googleAuthService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.jwtHandlerService = jwtHandlerService;
        this.githubAuthService = githubAuthService;
        this.googleAuthService = googleAuthService;
    }

    @Bean
    public BasicAuthenticationProvider basicAuthenticationProvider(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return new BasicAuthenticationProvider(bCryptPasswordEncoder, userDetailsService);
    }

    @Bean
    public SocialAuthenticationProvider<GithubAuthAuthenticationToken> githubAuthenticationProvider() {
        return new SocialAuthenticationProvider<>(githubAuthService, userService, GithubAuthAuthenticationToken.class);
    }

    @Bean
    public SocialAuthenticationProvider<GoogleAuthAuthenticationToken> googleAuthenticationProvider() {
        return new SocialAuthenticationProvider<>(googleAuthService, userService, GoogleAuthAuthenticationToken.class);
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
