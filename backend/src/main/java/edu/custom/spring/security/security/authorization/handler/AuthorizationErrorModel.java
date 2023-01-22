package edu.custom.spring.security.security.authorization.handler;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorizationErrorModel<T extends RuntimeException> {

    private Integer code;
    private String status;
    private String message;
}
