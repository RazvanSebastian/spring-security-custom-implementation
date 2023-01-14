package edu.custom.spring.security.security.authorization.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationDeniedHandler {
    private static final Logger logger = LoggerFactory.getLogger(AccessDeniedHandlerImpl.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public void handle(HttpServletResponse response, HttpStatus httpStatus, String message) throws IOException {
        if (response.isCommitted()) {
            logger.debug("Did not write to response since already committed");
            return;
        }
        SecurityContextHolder.clearContext();

        AuthorizationErrorModel errorModel = new AuthorizationErrorModel();
        errorModel.setMessage(message);
        errorModel.setCode(httpStatus.value());
        errorModel.setStatus(httpStatus.name());

//        response.setContentType(MediaType.APPLICATION_JSON.getType());
        response.setHeader("Content-Type", "application/json");
        response.setStatus(httpStatus.value());
        response.getWriter().write(OBJECT_MAPPER.writeValueAsString(errorModel));
        response.getWriter().flush();

        logger.debug(String.format("Responding with %d status code", httpStatus.value()));
    }
}
