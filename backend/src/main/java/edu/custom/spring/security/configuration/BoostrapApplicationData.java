package edu.custom.spring.security.configuration;

import edu.custom.spring.security.model.entity.resource.SecuredResource;
import edu.custom.spring.security.model.entity.security.*;
import edu.custom.spring.security.repository.resource.SecuredResourceRepository;
import edu.custom.spring.security.repository.security.PrivilegeRepository;
import edu.custom.spring.security.repository.security.RoleRepository;
import edu.custom.spring.security.repository.security.UserInfoRepository;
import edu.custom.spring.security.repository.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Transactional
@Component
public class BoostrapApplicationData implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final SecuredResourceRepository securedResourceRepository;
    private final UserInfoRepository userInfoRepository;

    @Autowired
    public BoostrapApplicationData(UserRepository userRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository, SecuredResourceRepository securedResourceRepository, UserInfoRepository userInfoRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.securedResourceRepository = securedResourceRepository;
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        privilegeRepository.deleteAll();

        Privilege readPrivilege = addPrivilege(PrivilegesEnum.READ);
        Privilege writePrivilege = addPrivilege(PrivilegesEnum.WRITE);

        Role adminRole = addRole(RolesEnum.ADMIN, readPrivilege, writePrivilege);
        Role userRole = addRole(RolesEnum.USER, readPrivilege);

        addUser("admin@security.com", "admin", adminRole);
        addUser("user@security.com", "user", userRole);
    }

    private void addUser(String email, String password, Role... roles) {
        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(email, null));
        SecurityContextHolder.setContext(securityContext);

        UserInfo userInfo = userInfoRepository.save(UserInfo.builder().email(email).build());

        User user = new User();
        user.setAuthenticationType(AuthenticationType.BASIC);
        user.setUsername(email);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setRoles(Arrays.asList(roles));
        user.setUserInfo(userInfo);
        addSecuredResources(userRepository.save(user));

        SecurityContextHolder.clearContext();
    }

    private Role addRole(RolesEnum authority, Privilege... privileges) {
        Role role = new Role();
        role.setAuthority(authority);
        role.setPrivileges(Arrays.asList(privileges));
        return roleRepository.save(role);
    }

    private Privilege addPrivilege(PrivilegesEnum authority) {
        Privilege privilege = new Privilege();
        privilege.setAuthority(authority);
        return privilegeRepository.save(privilege);
    }

    private void addSecuredResources(User user) {
        List<SecuredResource> securedResources = IntStream.rangeClosed(0, 5)
                .mapToObj(intValue -> {
                    SecuredResource securedResource = new SecuredResource();
                    securedResource.setValue("item" + intValue);
                    securedResource.setUser(user);
                    return securedResource;
                })
                .collect(Collectors.toList());
        securedResourceRepository.saveAll(securedResources);

    }
}
