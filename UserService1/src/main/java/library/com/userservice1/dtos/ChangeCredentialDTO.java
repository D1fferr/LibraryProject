package library.com.userservice1.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeCredentialDTO {

    private String email;
    private String username;

}
