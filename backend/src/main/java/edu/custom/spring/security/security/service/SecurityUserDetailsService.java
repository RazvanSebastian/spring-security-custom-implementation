package edu.custom.spring.security.security.service;

import edu.custom.spring.security.model.security.Role;
import edu.custom.spring.security.model.security.User;
import edu.custom.spring.security.repository.RoleRepository;
import edu.custom.spring.security.repository.UserRepository;
import edu.custom.spring.security.security.authentication.social.model.SocialAuthentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Objects;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public SecurityUserDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserDetails userDetails = userRepository.findByEmail(username);
        if (!isEmpty(userDetails)) {
            return userDetails;
        } else {
            throw new UsernameNotFoundException(String.format("User with email %s does not exist!", username));
        }
    }

    /**
     * If the user already sing up before with provided social account then retrieve it; otherwise create new account.
     *
     * @param authentication
     * @return
     */
    @Transactional
    public UserDetails retrieveOrRegisterNewUserWithSocialAuth(SocialAuthentication authentication){
        User user = userRepository.findByEmail(authentication.getUsername());
        if(Objects.isNull(user)) {
            final Role userRole = roleRepository.findByAuthority("ROLE_USER");
            user = new User();
            user.setEmail(authentication.getUsername());
            user.setRoles(Arrays.asList(userRole));
            return userRepository.save(user);
        } else {
            return user;
        }
    }

}
