package com.Library.UserService.dto;

import jdk.jfr.Name;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserDtoWithRole {
    private List<UserDtoWithRoleAndID> role;
}
