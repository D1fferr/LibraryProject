package com.Library.AuthService.repositories;

import com.Library.AuthService.models.AuthUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, UUID> {
    Optional<AuthUser> findFirstByUsername(@NotNull(message = "Field username must not be empty") String username);

    Optional<AuthUser> findByUsernameOrEmail(@NotNull(message = "Field username must not be empty") String username, @Email(message = "Email should be in format email@email.email") String email);

    List<AuthUser> findAllByIdIn(Collection<UUID> ids);
}
