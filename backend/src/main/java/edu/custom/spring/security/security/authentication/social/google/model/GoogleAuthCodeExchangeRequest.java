package edu.custom.spring.security.security.authentication.social.google.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Builder
public class GoogleAuthCodeExchangeRequest {

    @Getter
    private String code;

    @Getter(onMethod_ = {@JsonGetter(value = "client_id")})
    private String clientId;

    @Getter(onMethod_ = {@JsonGetter(value = "client_secret")})
    private String clientSecret;

    @Getter(onMethod_ = {@JsonGetter(value = "redirect_uri")})
    private String redirectUri;

    @Getter(onMethod_ = {@JsonGetter(value = "grant_type")})
    private String grantType;
}
