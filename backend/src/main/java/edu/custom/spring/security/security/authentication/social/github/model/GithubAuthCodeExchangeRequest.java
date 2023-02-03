package edu.custom.spring.security.security.authentication.social.github.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Builder
public class GithubAuthCodeExchangeRequest {

    @Getter
    private String code;

    @Getter(onMethod_ = {@JsonGetter(value = "client_id")})
    private String clientId;

    @Getter(onMethod_ = {@JsonGetter(value = "client_secret")})
    private String clientSecret;

    @Getter
    private String accept;
}
