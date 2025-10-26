package com.Library.UserService.repositories;

import com.Library.UserService.models.AuthUser;
import com.Library.UserService.models.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, UUID> {
    Optional<AuthUser> findFirstByUsername(@NotNull(message = "Field username must not be empty") String username);
}
