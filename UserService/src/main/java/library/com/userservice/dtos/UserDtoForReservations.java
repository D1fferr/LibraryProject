package library.com.userservice.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class UserDtoForReservations {
    private List<UUID> userDTOForViews;
}
