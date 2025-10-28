package library.com.userservice1.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    @NotNull(message = "User id must not be empty")
    private String id;
    @NotNull(message = "Username must not be empty")
    private String username;
    @Email(message = "Email should be in format email@email.email")
    private String email;
    private String libraryCode;
}
