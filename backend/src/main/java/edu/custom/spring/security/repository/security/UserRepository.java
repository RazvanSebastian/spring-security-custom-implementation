package edu.custom.spring.security.repository.security;

import edu.custom.spring.security.model.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT user FROM User user WHERE user.email = :email ")
    User findByEmail(@Param("email") String email);
}
