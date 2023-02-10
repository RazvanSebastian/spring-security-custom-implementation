package edu.custom.spring.security.service.security;

import edu.custom.spring.security.mapper.UserMapper;
import edu.custom.spring.security.model.entity.dto.PageDto;
import edu.custom.spring.security.model.entity.dto.security.UserClaimsDto;
import edu.custom.spring.security.model.entity.dto.security.UserDto;
import edu.custom.spring.security.model.entity.security.Role;
import edu.custom.spring.security.model.entity.security.RolesEnum;
import edu.custom.spring.security.model.entity.security.User;
import edu.custom.spring.security.model.entity.security.UserInfo;
import edu.custom.spring.security.repository.security.RoleRepository;
import edu.custom.spring.security.repository.security.UserInfoRepository;
import edu.custom.spring.security.repository.security.UserRepository;
import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthUserInfoResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserInfoRepository userInfoRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userInfoRepository = userInfoRepository;
        this.userMapper = userMapper;
    }

    /**
     * If the user already signed up before, with provided social account, then retrieve it; otherwise create new account.
     *
     * @param authentication
     * @return
     */
    @Transactional
    @Override
    public UserDetails getOrSave(SocialAuthUserInfoResponse authentication) {
        final String socialUsername = authentication.getUsername();
        Optional<User> optionalUser = userRepository.find(socialUsername);
        if (optionalUser.isEmpty()) {
            final Role userRole = roleRepository.findByAuthority(RolesEnum.USER);
            final UserInfo userInfo = saveUserInfo(authentication);
            // Save new user signed in with social platform
            final User user = User.builder()
                    .username(socialUsername)
                    .roles(Arrays.asList(userRole))
                    .userInfo(userInfo)
                    .authenticationType(authentication.getAuthenticationType())
                    .build();
            return userRepository.save(user);
        } else {
            return optionalUser.get();
        }
    }

    @Override
    public User getAuthenticatedUser() {
        final String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.find(principal).orElseThrow(() -> new BadCredentialsException("Authorization failed"));
    }

    @Override
    public UserClaimsDto getUserClaims() {
        final User authenticatedUser = getAuthenticatedUser();
        return this.userMapper.mapToUserClaims(authenticatedUser);
    }

    @Override
    public PageDto<UserDto> getUsers(final String searchedUserName, final Integer pageIndex, final Integer pageSize, final Sort.Direction sortDirection) {
        Sort sort = Sort.by("username");
        if (sortDirection.isAscending()) {
            sort = sort.ascending();
        }
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        Page<User> page = userRepository.findAll(searchedUserName, pageable);
        return PageDto.<UserDto>builder()
                .currentPage(page.getNumber())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .content(userMapper.mapTo(page.getContent()))
                .build();
    }

    private UserInfo saveUserInfo(@NotNull SocialAuthUserInfoResponse authentication) {
        UserInfo userInfo = UserInfo.builder()
                .email(authentication.getEmail())
                .familyName(authentication.getFamilyName())
                .givenName(authentication.getGivenName())
                .picture(authentication.getPicture())
                .build();
        return userInfoRepository.save(userInfo);
    }

}
