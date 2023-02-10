package edu.custom.spring.security.service.resource;

import edu.custom.spring.security.model.entity.dto.PageDto;
import edu.custom.spring.security.model.entity.resource.SecuredResource;
import edu.custom.spring.security.model.entity.security.User;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface SecuredResourceService {

    List<SecuredResource> getAllSecuredResources(final User user);

    PageDto<SecuredResource> getAllSecuredResources(final String searchedUserName, final String searchedValue, final Integer pageIndex, final Integer pageSize, final Sort.Direction sortDirection);

    SecuredResource save(String value, User user);

    void delete(Long id);
}
