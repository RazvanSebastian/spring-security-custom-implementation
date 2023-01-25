package edu.custom.spring.security.configuration;

import edu.custom.spring.security.model.security.Privilege;
import edu.custom.spring.security.model.security.Role;
import edu.custom.spring.security.model.security.User;
import edu.custom.spring.security.repository.PrivilegeRepository;
import edu.custom.spring.security.repository.RoleRepository;
import edu.custom.spring.security.repository.UserRepository;
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

        Privilege readPrivilege = addPrivilege("READ");
        Privilege writePrivilege = addPrivilege("WRITE");

        Role adminRole = addRole("ROLE_ADMIN", readPrivilege, writePrivilege);
        Role userRole = addRole("ROLE_USER", readPrivilege);

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

    private Role addRole(String authority, Privilege... privileges) {
        Role role = new Role();
        role.setAuthority(authority);
        role.setPrivileges(Arrays.asList(privileges));
        return roleRepository.save(role);
    }

    private Privilege addPrivilege(String authority) {
        Privilege privilege = new Privilege();
        privilege.setAuthority(authority);
        return privilegeRepository.save(privilege);
    }
}
