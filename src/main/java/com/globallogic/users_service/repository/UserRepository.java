package com.globallogic.users_service.repository;

import com.globallogic.users_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The UserRepository interface provides methods to interact with the database for user-related
 * operations.
 */
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

}
