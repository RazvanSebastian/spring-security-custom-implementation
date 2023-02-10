package edu.custom.spring.security.security.authentication.social.github.client;

import edu.custom.spring.security.security.authentication.social.base.client.SocialAuthClient;
import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthCodeExchangeRequest;
import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthCodeExchangeResponse;
import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthUserInfoResponse;
import edu.custom.spring.security.security.authentication.social.github.config.GithubAuthProperties;
import edu.custom.spring.security.security.authentication.social.github.model.GithubAuthCodeExchangeResponse;
import edu.custom.spring.security.security.authentication.social.github.model.GithubAuthUserInfoResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubAuthClient implements SocialAuthClient {

    private final RestTemplate restTemplate;
    private final GithubAuthProperties githubAuthProperties;

    public GithubAuthClient(RestTemplate restTemplate, GithubAuthProperties githubAuthProperties) {
        this.restTemplate = restTemplate;
        this.githubAuthProperties = githubAuthProperties;
    }

    @Override
    public SocialAuthCodeExchangeResponse requestForAccessToken(SocialAuthCodeExchangeRequest request){
        final String uri = githubAuthProperties.getTokenUri();
        ResponseEntity<GithubAuthCodeExchangeResponse> response = restTemplate.postForEntity(uri, request, GithubAuthCodeExchangeResponse.class);
        return response.getBody();
    }

    @Override
    public SocialAuthUserInfoResponse requestForUserInfo(String accessToken) {
        final String uri = githubAuthProperties.getUserInfoUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);

        ResponseEntity<GithubAuthUserInfoResponse> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, GithubAuthUserInfoResponse.class);
        return response.getBody();
    }

}
