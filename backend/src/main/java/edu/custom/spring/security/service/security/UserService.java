package edu.custom.spring.security.service.security;

import edu.custom.spring.security.model.entity.security.User;
import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthUserInfoResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    UserDetails getOrSave(SocialAuthUserInfoResponse authentication);

    User getAuthenticatedUser();

}
