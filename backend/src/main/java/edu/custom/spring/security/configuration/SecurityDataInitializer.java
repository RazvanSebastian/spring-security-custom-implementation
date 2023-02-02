package edu.custom.spring.security.configuration;

import edu.custom.spring.security.model.security.*;
import edu.custom.spring.security.repository.security.PrivilegeRepository;
import edu.custom.spring.security.repository.security.RoleRepository;
import edu.custom.spring.security.repository.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SecurityDataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;

    @Autowired
    public  SecurityDataInitializer(UserRepository userRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
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
        User user = new User();
        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setRoles(Arrays.asList(roles));
        userRepository.save(user);
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
}
