package edu.custom.spring.security.security.authentication.google.config;

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
@ConfigurationProperties(prefix = "security.auth.google")
public class GoogleAuthProperties {

    private String clientId;
    private String projectId;
    private String scopes;
    private String authUri;
    private String tokenUri;
    private String userInfo;
    private String authProviderX509CertUrl;
    private String clientSecret;
    private String redirectAuthConsentCallbackUri;

    private UriComponents googleAuthConsentRequestUri;

    public String getUserInfoRequestUri(String accessToken) {
        return UriComponentsBuilder.fromHttpUrl(userInfo)
                .queryParam("alt", "json")
                .queryParam("access_token", accessToken)
                .build()
                .toUriString();
    }

    @PostConstruct
    public void initializeRequestUris() {
        this.initializeGoogleAuthConsentRequestUri();
    }

    private void initializeGoogleAuthConsentRequestUri() {
        this.googleAuthConsentRequestUri = UriComponentsBuilder
                .fromHttpUrl(authUri)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectAuthConsentCallbackUri)
                .queryParam("response_type", "code")
                .queryParam("include_granted_scopes", true)
                .queryParam("scope", scopes)
                .queryParam("access_type", "offline")
                .build();
    }
}
