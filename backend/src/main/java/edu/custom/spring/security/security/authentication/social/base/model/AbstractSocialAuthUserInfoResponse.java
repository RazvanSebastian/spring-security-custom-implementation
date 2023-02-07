package edu.custom.spring.security.security.authentication.social.base.model;

public abstract class AbstractSocialAuthUserInfoResponse implements SocialAuthUserInfoResponse {

    /**
     * Return a join between email and authenticationType, where join char is '_'
     *
     * @return expected username for a specific authenticationType
     */
    @Override
    public final String getUsername() {
        return getEmail() + "_" + getAuthenticationType().getValue();
    }
}
