package edu.custom.spring.security.security.authentication.social.github.service;

import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthCodeExchangeResponse;
import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthConsentUri;
import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthUserInfoResponse;
import edu.custom.spring.security.security.authentication.social.base.service.SocialAuthService;
import edu.custom.spring.security.security.authentication.social.github.client.GithubAuthClient;
import edu.custom.spring.security.security.authentication.social.github.config.GithubAuthProperties;
import edu.custom.spring.security.security.authentication.social.github.model.GithubAuthCodeExchangeRequest;
import org.springframework.stereotype.Service;

@Service
public class GithubAuthService implements SocialAuthService {

    private final GithubAuthProperties githubAuthProperties;
    private final GithubAuthClient githubAuthClient;

    public GithubAuthService(GithubAuthProperties githubAuthProperties, GithubAuthClient githubAuthClient) {
        this.githubAuthProperties = githubAuthProperties;
        this.githubAuthClient = githubAuthClient;
    }

    @Override
    public SocialAuthUserInfoResponse getUserInfo(String authorizationCode) {
        GithubAuthCodeExchangeRequest request = GithubAuthCodeExchangeRequest.builder()
                .code(authorizationCode)
                .clientId(githubAuthProperties.getClientId())
                .clientSecret(githubAuthProperties.getClientSecret())
                .accept("json")
                .build();
        SocialAuthCodeExchangeResponse authCodeExchangeResponse = githubAuthClient.requestForAccessToken(request);
        return githubAuthClient.requestForUserInfo(authCodeExchangeResponse.getAccessToken());
    }

    @Override
    public SocialAuthConsentUri getConsentAuthUri() {
        return new SocialAuthConsentUri(this.githubAuthProperties.getGithubAuthConsentRequestUri().toUriString());
    }
}
