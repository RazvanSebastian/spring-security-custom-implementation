package edu.custom.spring.security.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.Authentication;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtClaims {

    private Authentication authentication;

}
