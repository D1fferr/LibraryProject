package com.Library.UserService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDTO {
    private String code;
    private String param;
    private String newPassword;
}
