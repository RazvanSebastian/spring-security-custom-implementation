package edu.custom.spring.security.repository.resource;

import edu.custom.spring.security.model.entity.resource.SecuredResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecuredResourceRepository extends JpaRepository<SecuredResource, Long> {

    @Query("SELECT resource FROM SecuredResource resource JOIN FETCH resource.user user WHERE user.id = :user_id")
    List<SecuredResource> findAll(@Param("user_id") Long userId);

    @Query(value = "SELECT resource FROM SecuredResource resource WHERE resource.value LIKE %:searchedValue% AND resource.audit.createdBy LIKE %:userName%")
    Page<SecuredResource> findAll(@Param("searchedValue") String searchedValue, @Param("userName") String userName, Pageable pageable);
}
