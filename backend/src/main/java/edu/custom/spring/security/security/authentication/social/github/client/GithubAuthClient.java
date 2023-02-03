package edu.custom.spring.security.security.authentication.social.github.client;

import edu.custom.spring.security.security.authentication.social.github.config.GithubAuthProperties;
import edu.custom.spring.security.security.authentication.social.github.model.GithubAuthCodeExchangeRequest;
import edu.custom.spring.security.security.authentication.social.github.model.GithubAuthCodeExchangeResponse;
import edu.custom.spring.security.security.authentication.social.github.model.GithubUserInfoResponse;
import edu.custom.spring.security.security.authentication.social.google.config.GoogleAuthProperties;
import edu.custom.spring.security.security.authentication.social.google.model.GoogleAuthCodeExchangeRequest;
import edu.custom.spring.security.security.authentication.social.google.model.GoogleAuthCodeExchangeResponse;
import edu.custom.spring.security.security.authentication.social.google.model.GoogleUserInfoResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubAuthClient {

    private final RestTemplate restTemplate;
    private final GithubAuthProperties githubAuthProperties;

    public GithubAuthClient(RestTemplate restTemplate, GithubAuthProperties githubAuthProperties) {
        this.restTemplate = restTemplate;
        this.githubAuthProperties = githubAuthProperties;
    }

    public GithubAuthCodeExchangeResponse requestForAccessToken(GithubAuthCodeExchangeRequest request){
        final String uri = githubAuthProperties.getTokenUri();
        ResponseEntity<GithubAuthCodeExchangeResponse> response = restTemplate.postForEntity(uri, request, GithubAuthCodeExchangeResponse.class);
        return response.getBody();
    }

    public GithubUserInfoResponse requestForUserInfo(String accessToken) {
        final String uri = githubAuthProperties.getUserInfoUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);

        ResponseEntity<GithubUserInfoResponse> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, GithubUserInfoResponse.class);
        return response.getBody();
    }

}
