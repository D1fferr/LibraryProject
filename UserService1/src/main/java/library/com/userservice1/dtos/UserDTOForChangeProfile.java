package library.com.userservice1.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTOForChangeProfile {
    @Email(message = "Email should be in format email@email.email")
    private String email;
    private String libraryCode;
}
