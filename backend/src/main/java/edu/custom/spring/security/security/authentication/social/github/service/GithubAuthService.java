package edu.custom.spring.security.security.authentication.social.github.service;

import edu.custom.spring.security.security.authentication.social.github.client.GithubAuthClient;
import edu.custom.spring.security.security.authentication.social.github.config.GithubAuthProperties;
import edu.custom.spring.security.security.authentication.social.github.model.GithubAuthCodeExchangeRequest;
import edu.custom.spring.security.security.authentication.social.github.model.GithubAuthCodeExchangeResponse;
import edu.custom.spring.security.security.authentication.social.github.model.GithubAuthConsentUriRequest;
import edu.custom.spring.security.security.authentication.social.github.model.GithubUserInfoResponse;
import org.springframework.stereotype.Service;

@Service
public class GithubAuthService {

    private final GithubAuthProperties githubAuthProperties;
    private final GithubAuthClient githubAuthClient;

    public GithubAuthService(GithubAuthProperties githubAuthProperties, GithubAuthClient githubAuthClient) {
        this.githubAuthProperties = githubAuthProperties;
        this.githubAuthClient = githubAuthClient;
    }

    public GithubAuthConsentUriRequest getConsentAuthUriRequest() {
        return new GithubAuthConsentUriRequest(this.githubAuthProperties.getGithubAuthConsentRequestUri().toUriString());
    }

    public GithubUserInfoResponse getUserInfo(String authorizationCode) {
        GithubAuthCodeExchangeRequest request = GithubAuthCodeExchangeRequest.builder()
                .code(authorizationCode)
                .clientId(githubAuthProperties.getClientId())
                .clientSecret(githubAuthProperties.getClientSecret())
                .accept("json")
                .build();
        GithubAuthCodeExchangeResponse authCodeExchangeResponse = githubAuthClient.requestForAccessToken(request);
        return githubAuthClient.requestForUserInfo(authCodeExchangeResponse.getAccessToken());
    }
}
