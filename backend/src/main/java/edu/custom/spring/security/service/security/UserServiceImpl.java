package edu.custom.spring.security.service.security;

import edu.custom.spring.security.model.security.*;
import edu.custom.spring.security.repository.security.RoleRepository;
import edu.custom.spring.security.repository.security.UserInfoRepository;
import edu.custom.spring.security.repository.security.UserRepository;
import edu.custom.spring.security.security.authentication.social.base.model.SocialAuthUserInfoResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserInfoRepository userInfoRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserInfoRepository userInfoRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userInfoRepository = userInfoRepository;
    }

    /**
     * If the user already sing up before with provided social account then retrieve it; otherwise create new account.
     *
     * @param authentication
     * @return
     */
    @Transactional
    @Override
    public UserDetails getOrSave(SocialAuthUserInfoResponse authentication, AuthenticationType authenticationType){
        final String socialUsername = authentication.getEmail() + "_" + authenticationType.getValue();
        User user = userRepository.find(socialUsername);
        if(Objects.isNull(user)) {
            final Role userRole = roleRepository.findByAuthority(RolesEnum.USER);
            // Save info received from social platform
            UserInfo userInfo = UserInfo.builder()
                    .email(authentication.getEmail())
                    .familyName(authentication.getFamilyName())
                    .givenName(authentication.getGivenName())
                    .picture(authentication.getPicture())
                    .build();
            userInfo = userInfoRepository.save(userInfo);
            // Save new user signed in with social platform
            user = User.builder()
                    .username(socialUsername)
                    .roles(Arrays.asList(userRole))
                    .userInfo(userInfo)
                    .authenticationType(authenticationType)
                    .build();
            user = userRepository.save(user);
        }
        return user;
    }

    @Override
    public User getAuthenticatedUser() {
        final String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final UserDetails userDetails = userRepository.find(principal);
        return (User) userDetails;
    }
}
