package edu.custom.spring.security.security.authentication.social.google.service;

import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthCodeExchangeResponse;
import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthConsentUri;
import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthUserInfoResponse;
import edu.custom.spring.security.security.authentication.social.base.service.SocialAuthService;
import edu.custom.spring.security.security.authentication.social.google.client.GoogleAuthClient;
import edu.custom.spring.security.security.authentication.social.google.config.GoogleAuthProperties;
import edu.custom.spring.security.security.authentication.social.google.model.GoogleAuthCodeExchangeRequest;
import org.springframework.stereotype.Service;

@Service
public class GoogleAuthService implements SocialAuthService {

    private final GoogleAuthProperties googleAuthProperties;
    private final GoogleAuthClient googleAuthClient;

    public GoogleAuthService(GoogleAuthProperties googleAuthProperties, GoogleAuthClient googleAuthClient) {
        this.googleAuthProperties = googleAuthProperties;
        this.googleAuthClient = googleAuthClient;
    }

    @Override
    public SocialAuthConsentUri getConsentAuthUri() {
        return new SocialAuthConsentUri(this.googleAuthProperties.getGoogleAuthConsentRequestUri().toUriString());
    }

    @Override
    public SocialAuthUserInfoResponse getUserInfo(String authorizationCode) {
        GoogleAuthCodeExchangeRequest request = GoogleAuthCodeExchangeRequest.builder()
                .code(authorizationCode)
                .clientId(googleAuthProperties.getClientId())
                .clientSecret(googleAuthProperties.getClientSecret())
                .grantType("authorization_code")
                .redirectUri(googleAuthProperties.getRedirectAuthConsentCallbackUri())
                .build();
        SocialAuthCodeExchangeResponse authCodeExchangeResponse = this.googleAuthClient.requestForAccessToken(request);
        return googleAuthClient.requestForUserInfo(authCodeExchangeResponse.getAccessToken());
    }
}
