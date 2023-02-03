package edu.custom.spring.security.service.security;

import edu.custom.spring.security.model.security.User;
import edu.custom.spring.security.security.authentication.social.model.SocialUserInfo;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    UserDetails getOrSaveNewUserBySocialAuthentication(SocialUserInfo authentication);

    User getAuthenticatedUser();

}
