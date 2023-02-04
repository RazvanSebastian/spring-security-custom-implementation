package edu.custom.spring.security.security.authentication.social.github.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthCodeExchangeResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubAuthCodeExchangeResponse implements SocialAuthCodeExchangeResponse {

    @JsonAlias("access_token")
    private String accessToken;

    @JsonAlias("expires_in")
    private String expiresIn;

    @JsonAlias("refresh_token")
    private String refreshToken;

    @JsonAlias("scope")
    private String scope;

    @JsonAlias("token_type")
    private String tokenType;
}
