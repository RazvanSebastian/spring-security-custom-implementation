package edu.custom.spring.security.security.authentication.social.base.model;

public interface SocialAuthUserInfoResponse extends SocialAuthenticationType{

    String getEmail();
    String getFamilyName();
    String getGivenName();
    String getPicture();

}
