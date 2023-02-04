package edu.custom.spring.security.security.authentication.social.base.model;

public interface SocialAuthCodeExchangeRequest {

    String getCode();

    String getClientId();

    String getClientSecret();
}
