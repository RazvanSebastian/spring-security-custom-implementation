package edu.custom.spring.security.service.security;

import edu.custom.spring.security.model.security.User;
import edu.custom.spring.security.security.authentication.social.model.SocialAuthentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    UserDetails getOrSaveNewUserBySocialAuthentication(SocialAuthentication authentication);

    User getAuthenticatedUser();

}
