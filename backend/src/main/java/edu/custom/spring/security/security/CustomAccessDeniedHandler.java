package edu.custom.spring.security.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.custom.spring.security.model.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (!response.isCommitted()) {
            try {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setStatus(HttpStatus.FORBIDDEN);
                errorResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
                errorResponse.setMessage("Forbidden");
                objectMapper.writeValue(response.getWriter(), errorResponse);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
