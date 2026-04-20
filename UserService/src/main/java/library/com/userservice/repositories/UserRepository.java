package library.com.userservice.repositories;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import library.com.userservice.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findUserByEmailOrLibraryCodeOrUsername(String email, String libraryCode, String username);

    Page<User> findAllByIdIn(Collection<UUID> ids, Pageable pageable);

    Page<User> findAllByIdOrEmailOrUsernameOrLibraryCode(UUID id, String email, String username, String libraryCode, Pageable pageable);
    @Query("SELECT u FROM User u WHERE " +
            "str(u.id) LIKE :search OR " +
            "u.email LIKE :search OR " +
            "u.username LIKE :search OR " +
            "u.libraryCode LIKE :search")
    Page<User> searchEverywhere(String search, Pageable pageable);
}