package edu.custom.spring.security.service.resource;

import edu.custom.spring.security.model.resource.SecuredResource;
import edu.custom.spring.security.model.security.User;
import edu.custom.spring.security.repository.resource.SecuredResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecuredResourceServiceImpl implements SecuredResourceService {
    private final SecuredResourceRepository securedResourceRepository;

    public SecuredResourceServiceImpl(SecuredResourceRepository securedResourceRepository) {
        this.securedResourceRepository = securedResourceRepository;
    }

    @Override
    public List<SecuredResource> getAllSecuredResources() {
        return securedResourceRepository.findAll();
    }

    @Override
    public List<SecuredResource> getAllSecuredResources(User user) {
        return securedResourceRepository.findAll(user.getId());
    }

    @Override
    public SecuredResource save(String value, User user) {
        final SecuredResource securedResource = new SecuredResource();
        securedResource.setValue(value);
        securedResource.setUser(user);
        return securedResourceRepository.save(securedResource);
    }

    @Override
    public void delete(Long id) {
        securedResourceRepository.deleteById(id);
    }
}
