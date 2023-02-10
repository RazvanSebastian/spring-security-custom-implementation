package edu.custom.spring.security.security.authentication.social.github.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "security.auth.github")
public class GithubAuthProperties {

    private String clientId;
    private String clientSecret;
    private String projectName;
    private String scopes;
    private String authUri;
    private String tokenUri;
    private String userInfoUri;
    private UriComponents githubAuthConsentRequestUri;

    @PostConstruct
    private void init() {
        this.initializeGithubAuthConsentRequestUri();
    }

    private void initializeGithubAuthConsentRequestUri() {
        this.githubAuthConsentRequestUri = UriComponentsBuilder
                .fromHttpUrl(authUri)
                .queryParam("scope", scopes)
                .queryParam("client_id", clientId)
                .build();
    }

}
