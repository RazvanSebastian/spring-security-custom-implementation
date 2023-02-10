package edu.custom.spring.security.security.authentication.social.google.client;

import edu.custom.spring.security.security.authentication.social.base.client.SocialAuthClient;
import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthCodeExchangeRequest;
import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthCodeExchangeResponse;
import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthUserInfoResponse;
import edu.custom.spring.security.security.authentication.social.google.config.GoogleAuthProperties;
import edu.custom.spring.security.security.authentication.social.google.model.GoogleAuthCodeExchangeRequest;
import edu.custom.spring.security.security.authentication.social.google.model.GoogleAuthCodeExchangeResponse;
import edu.custom.spring.security.security.authentication.social.google.model.GoogleAuthUserInfoResponseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleAuthClient implements SocialAuthClient {

    private final RestTemplate restTemplate;
    private final GoogleAuthProperties googleAuthProperties;

    public GoogleAuthClient(RestTemplate restTemplate, GoogleAuthProperties googleAuthProperties) {
        this.restTemplate = restTemplate;
        this.googleAuthProperties = googleAuthProperties;
    }

    @Override
    public SocialAuthCodeExchangeResponse requestForAccessToken(SocialAuthCodeExchangeRequest request){
        final String uri = googleAuthProperties.getTokenUri();
        ResponseEntity<GoogleAuthCodeExchangeResponse> response = restTemplate.postForEntity(uri, request, GoogleAuthCodeExchangeResponse.class);
        return response.getBody();
    }

    @Override
    public SocialAuthUserInfoResponse requestForUserInfo(String accessToken) {
        final String uri = googleAuthProperties.getUserInfoRequestUri(accessToken);
        ResponseEntity<GoogleAuthUserInfoResponseResponse> response = restTemplate.getForEntity(uri, GoogleAuthUserInfoResponseResponse.class);
        return response.getBody();
    }

}
