package edu.custom.spring.security.security.authorization.handler;

import edu.custom.spring.security.security.SecurityUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    protected static final Log logger = LogFactory.getLog(AccessDeniedHandlerImpl.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (response.isCommitted()) {
            logger.debug("Did not write to response since already committed");
            return;
        }
        CustomAccessDeniedException deniedException = (CustomAccessDeniedException) accessDeniedException;
        response.addCookie(SecurityUtils.createAccessTokenCookie(null, true));
        response.setStatus(deniedException.getHttpStatus().value());
        response.getWriter().write(accessDeniedException.getMessage());
        response.getWriter().flush();

        logger.debug(String.format("Responding with %d status code", deniedException.getHttpStatus().value()));
    }
}
