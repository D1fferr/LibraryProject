package library.com.userservice.repositories;
import library.com.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findUserByEmailOrLibraryCodeOrUsername(String email, String libraryCode, String username);
}