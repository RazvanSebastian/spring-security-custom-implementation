package edu.custom.spring.security.service.resource;

import edu.custom.spring.security.model.resource.SecuredResource;
import edu.custom.spring.security.model.security.User;

import java.util.List;

public interface SecuredResourceService {

    List<SecuredResource> getAllSecuredResources();

    List<SecuredResource> getAllSecuredResources(User user);

    SecuredResource save(String value, User user);

    void delete(Long id);
}
