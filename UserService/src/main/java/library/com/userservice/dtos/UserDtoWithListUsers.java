package library.com.userservice.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserDtoWithListUsers {
    private List<UserDtoWithId> userDTOForViewList;
}