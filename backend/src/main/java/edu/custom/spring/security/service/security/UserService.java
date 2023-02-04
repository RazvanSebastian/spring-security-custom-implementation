package edu.custom.spring.security.service.security;

import edu.custom.spring.security.model.security.AuthenticationType;
import edu.custom.spring.security.model.security.User;
import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthUserInfoResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    UserDetails getOrSave(SocialAuthUserInfoResponse authentication, AuthenticationType authenticationType);

    User getAuthenticatedUser();

}
