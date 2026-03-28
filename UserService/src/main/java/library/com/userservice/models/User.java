package library.com.userservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@Table(name = "person")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @NotNull(message = "User id must not be null")
    @Column(name = "user_id")
    private UUID id;
    @Column(name = "user_name")
    @NotNull(message = "Username must not be empty")
    private String username;
    @Column(name = "user_email")
    @Email(message = "Email should be in format email@email.email")
    private String email;
    @Column(name = "user_library_code")
    private String libraryCode;

}