package edu.custom.spring.security.security.authorization.handler;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;

public class CustomAccessDeniedException extends AccessDeniedException {

    private HttpStatus httpStatus;

    public CustomAccessDeniedException(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
