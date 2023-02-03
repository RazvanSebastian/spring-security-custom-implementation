package edu.custom.spring.security.repository.security;

import edu.custom.spring.security.model.security.Role;
import edu.custom.spring.security.model.security.RolesEnum;
import edu.custom.spring.security.model.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT role FROM Role role WHERE role.authority = :authority")
    Role findByAuthority(@Param("authority") RolesEnum authority);
}
