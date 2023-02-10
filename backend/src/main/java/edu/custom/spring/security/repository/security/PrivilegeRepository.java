package edu.custom.spring.security.repository.security;

import edu.custom.spring.security.model.entity.security.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
}
