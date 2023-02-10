package edu.custom.spring.security.security.authentication.social.base.model;

import edu.custom.spring.security.model.entity.security.AuthenticationType;

public interface SocialAuthUserInfoResponse extends SocialAuthenticationType{

    String getEmail();
    String getFamilyName();
    String getGivenName();
    String getPicture();
    AuthenticationType getAuthenticationType();
    String getUsername();
}
