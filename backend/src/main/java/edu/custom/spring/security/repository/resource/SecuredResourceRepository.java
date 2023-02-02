package edu.custom.spring.security.repository.resource;

import edu.custom.spring.security.model.resource.SecuredResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecuredResourceRepository extends JpaRepository<SecuredResource, Long> {

    @Query("SELECT resource FROM SecuredResource resource JOIN FETCH resource.user user WHERE user.id = :user_id")
    List<SecuredResource> findAll(@Param("user_id") Long userId);
}
