package edu.custom.spring.security.model.entity.dto.security;

import edu.custom.spring.security.model.entity.security.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserClaimsDto {

    private UserInfo userInfo;
    private Set<GrantedAuthority> grantedAuthorities;
}
