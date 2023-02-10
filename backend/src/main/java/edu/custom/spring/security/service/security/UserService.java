package edu.custom.spring.security.service.security;

import edu.custom.spring.security.model.entity.dto.PageDto;
import edu.custom.spring.security.model.entity.dto.security.UserClaimsDto;
import edu.custom.spring.security.model.entity.dto.security.UserDto;
import edu.custom.spring.security.model.entity.security.User;
import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthUserInfoResponse;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    UserDetails getOrSave(SocialAuthUserInfoResponse authentication);

    User getAuthenticatedUser();

    UserClaimsDto getUserClaims();

    PageDto<UserDto> getUsers(final String searchedUserName, final Integer pageIndex, final Integer pageSize, final Sort.Direction sortDirection);

}
