package edu.custom.spring.security.security.authentication.social.base.client;

import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthCodeExchangeRequest;
import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthCodeExchangeResponse;
import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthUserInfoResponse;

public interface SocialAuthClient {

    SocialAuthCodeExchangeResponse requestForAccessToken(SocialAuthCodeExchangeRequest request);

    SocialAuthUserInfoResponse requestForUserInfo(String accessToken);
}
