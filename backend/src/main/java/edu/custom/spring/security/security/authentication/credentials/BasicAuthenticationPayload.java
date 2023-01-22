package edu.custom.spring.security.security.authentication.credentials;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * POJO class used on exchange of user credentials for a JWT access token.
 */
@Getter
@Setter
@NoArgsConstructor
public class BasicAuthenticationPayload {

    /**
     * Alias for Username
     */
    private String principal;

    /**
     * Alias for password
     */
    private String credentials;

}
