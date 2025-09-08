package com.Library.UserService.repositories;

import com.Library.UserService.models.User;
import jakarta.validation.constraints.NotEmpty;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserId(UUID personId);
    Optional<User> findByUsername(String username);
    @Query("SELECT p.userRole FROM User p WHERE p.username = :username")
    String findRole(@Param("username") String username);

    void deleteByUserId(UUID userId);
}
