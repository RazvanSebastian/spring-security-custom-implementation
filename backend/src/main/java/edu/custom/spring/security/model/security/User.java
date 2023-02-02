package edu.custom.spring.security.model.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
public class User implements UserDetails {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private boolean accountNonExpired;

    @JsonIgnore
    private boolean accountNonLocked;

    @JsonIgnore
    private boolean credentialsNonExpired;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userInfo_id", referencedColumnName = "id")
    private UserInfo userInfo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getGrantedAuthorities();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * The email will be used as username since it is unique also on social auth.
     * @return
     */
    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        return roles.stream()
                .map(role ->
                        role.getPrivileges()
                                .stream()
                                .map(privilege -> new SimpleGrantedAuthority(role.getAuthority().concat("_").concat(privilege.getAuthority())))
                                .collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toSet());
    }

}
