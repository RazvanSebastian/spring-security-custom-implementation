package edu.custom.spring.security.security.authentication.social.github.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthCodeExchangeRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Builder
public class GithubAuthCodeExchangeRequest implements SocialAuthCodeExchangeRequest {

    @Getter
    private String code;

    @Getter(onMethod_ = {@JsonGetter(value = "client_id")})
    private String clientId;

    @Getter(onMethod_ = {@JsonGetter(value = "client_secret")})
    private String clientSecret;

    @Getter
    private String accept;
}
