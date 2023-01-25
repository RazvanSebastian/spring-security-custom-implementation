package edu.custom.spring.security.security.authentication.social.google.client;

import edu.custom.spring.security.security.authentication.social.google.config.GoogleAuthProperties;
import edu.custom.spring.security.security.authentication.social.google.model.GoogleAuthCodeExchangeRequest;
import edu.custom.spring.security.security.authentication.social.google.model.GoogleAuthCodeExchangeResponse;
import edu.custom.spring.security.security.authentication.social.google.model.GoogleUserInfoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleAuthClient {

    private final RestTemplate restTemplate;
    private final GoogleAuthProperties googleAuthProperties;

    public GoogleAuthClient(RestTemplate restTemplate, GoogleAuthProperties googleAuthProperties) {
        this.restTemplate = restTemplate;
        this.googleAuthProperties = googleAuthProperties;
    }

    public GoogleAuthCodeExchangeResponse requestForAccessToken(GoogleAuthCodeExchangeRequest request){
        final String uri = googleAuthProperties.getTokenUri();
        ResponseEntity<GoogleAuthCodeExchangeResponse> response = restTemplate.postForEntity(uri, request, GoogleAuthCodeExchangeResponse.class);
        return response.getBody();
    }

    public GoogleUserInfoResponse requestForUserInfo(String accessToken) {
        final String uri = googleAuthProperties.getUserInfoRequestUri(accessToken);
        ResponseEntity<GoogleUserInfoResponse> response = restTemplate.getForEntity(uri, GoogleUserInfoResponse.class);
        return response.getBody();
    }

}
