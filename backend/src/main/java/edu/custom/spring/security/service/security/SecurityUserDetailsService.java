package edu.custom.spring.security.service.security;

import edu.custom.spring.security.repository.security.RoleRepository;
import edu.custom.spring.security.repository.security.UserInfoRepository;
import edu.custom.spring.security.repository.security.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserInfoRepository userInfoRepository;

    public SecurityUserDetailsService(UserRepository userRepository, RoleRepository roleRepository, UserInfoRepository userInfoRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserDetails userDetails = userRepository.find(username);
        if (!isEmpty(userDetails)) {
            return userDetails;
        } else {
            throw new UsernameNotFoundException(String.format("User with email %s does not exist!", username));
        }
    }



}
