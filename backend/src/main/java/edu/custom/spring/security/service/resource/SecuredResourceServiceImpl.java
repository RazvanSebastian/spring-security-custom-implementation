package edu.custom.spring.security.service.resource;

import edu.custom.spring.security.model.entity.dto.PageDto;
import edu.custom.spring.security.model.entity.resource.SecuredResource;
import edu.custom.spring.security.model.entity.security.User;
import edu.custom.spring.security.repository.resource.SecuredResourceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecuredResourceServiceImpl implements SecuredResourceService {
    private final SecuredResourceRepository securedResourceRepository;

    public SecuredResourceServiceImpl(SecuredResourceRepository securedResourceRepository) {
        this.securedResourceRepository = securedResourceRepository;
    }

    @Override
    public List<SecuredResource> getAllSecuredResources(final User user) {
        return securedResourceRepository.findAll(user.getId());
    }

    @Override
    public PageDto<SecuredResource> getAllSecuredResources(final String searchedUserName, final String searchedValue, final Integer pageIndex, final Integer pageSize, final Sort.Direction sortDirection) {
        Sort sort = Sort.by("value");
        if (sortDirection.isAscending()) {
            sort = sort.ascending();
        }
        Pageable sortedByResourceValue = PageRequest.of(pageIndex, pageSize, sort);
        Page<SecuredResource> page = securedResourceRepository.findAll(searchedValue, searchedUserName, sortedByResourceValue);
        return PageDto.<SecuredResource>builder()
                .currentPage(page.getNumber())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .content(page.getContent())
                .build();
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
