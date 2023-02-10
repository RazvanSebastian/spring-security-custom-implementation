package edu.custom.spring.security.security.authentication.social.base.service;

import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthConsentUri;
import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthUserInfoResponse;

public interface SocialAuthService {

    SocialAuthConsentUri getConsentAuthUri();

    SocialAuthUserInfoResponse getUserInfo(String authorizationCode);
}
