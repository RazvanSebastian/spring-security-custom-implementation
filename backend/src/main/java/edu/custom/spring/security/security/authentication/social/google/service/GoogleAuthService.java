package edu.custom.spring.security.security.authentication.social.google.service;

import edu.custom.spring.security.security.authentication.social.google.client.GoogleAuthClient;
import edu.custom.spring.security.security.authentication.social.google.config.GoogleAuthProperties;
import edu.custom.spring.security.security.authentication.social.google.model.GoogleAuthCodeExchangeRequest;
import edu.custom.spring.security.security.authentication.social.google.model.GoogleAuthCodeExchangeResponse;
import edu.custom.spring.security.security.authentication.social.google.model.GoogleAuthConsentUriResponse;
import edu.custom.spring.security.security.authentication.social.google.model.GoogleUserInfoResponse;
import org.springframework.stereotype.Service;

@Service
public class GoogleAuthService {

    private final GoogleAuthProperties googleAuthProperties;
    private final GoogleAuthClient googleAuthClient;

    public GoogleAuthService(GoogleAuthProperties googleAuthProperties, GoogleAuthClient googleAuthClient) {
        this.googleAuthProperties = googleAuthProperties;
        this.googleAuthClient = googleAuthClient;
    }

    public GoogleAuthConsentUriResponse getGoogleAuthConsentRequestUri() {
        return new GoogleAuthConsentUriResponse(this.googleAuthProperties.getGoogleAuthConsentRequestUri().toUriString());
    }

    public GoogleUserInfoResponse getUserInfo(String authorizationCode) {
        GoogleAuthCodeExchangeRequest request = GoogleAuthCodeExchangeRequest.builder()
                .code(authorizationCode)
                .clientId(googleAuthProperties.getClientId())
                .clientSecret(googleAuthProperties.getClientSecret())
                .grantType("authorization_code")
                .redirectUri(googleAuthProperties.getRedirectAuthConsentCallbackUri())
                .build();
        GoogleAuthCodeExchangeResponse authCodeExchangeResponse = this.googleAuthClient.requestForAccessToken(request);
        return googleAuthClient.requestForUserInfo(authCodeExchangeResponse.getAccessToken());
    }
}
