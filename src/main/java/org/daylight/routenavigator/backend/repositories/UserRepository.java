package org.daylight.routenavigator.backend.repositories;

import org.daylight.routenavigator.backend.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
//    @Query("SELECT u FROM User u WHERE u.email = :email AND u.password = :password")
    Optional<User> findByEmail(@Param("email") String email);
    Optional<User> findByUsername(@Param("username") String username);
}
