package edu.custom.spring.security.service.security;

import edu.custom.spring.security.model.security.Role;
import edu.custom.spring.security.model.security.RolesEnum;
import edu.custom.spring.security.model.security.User;
import edu.custom.spring.security.model.security.UserInfo;
import edu.custom.spring.security.repository.security.RoleRepository;
import edu.custom.spring.security.repository.security.UserInfoRepository;
import edu.custom.spring.security.repository.security.UserRepository;
import edu.custom.spring.security.security.authentication.social.model.SocialAuthentication;
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
    public UserDetails getOrSaveNewUserBySocialAuthentication(SocialAuthentication authentication){
        User user = userRepository.findByEmail(authentication.getEmail());
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
            user = new User();
            user.setEmail(authentication.getEmail());
            user.setRoles(Arrays.asList(userRole));
            user.setUserInfo(userInfo);

            user = userRepository.save(user);
        }
        return user;
    }

    @Override
    public User getAuthenticatedUser() {
        final String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final UserDetails userDetails = userRepository.findByEmail(principal);
        return (User) userDetails;
    }
}
