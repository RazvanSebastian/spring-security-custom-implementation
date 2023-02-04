package edu.custom.spring.security.security.authentication.social.base.model;

public interface SocialAuthCodeExchangeResponse {

    String getAccessToken();

    String getExpiresIn();

    String getRefreshToken();

    String getScope();

    String getTokenType();
}
