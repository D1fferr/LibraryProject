package com.Library.UserService.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class UserDtoWithRoleAndID {
    private UUID id;
    private String role;
}
