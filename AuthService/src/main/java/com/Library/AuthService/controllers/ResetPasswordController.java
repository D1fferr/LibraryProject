package com.Library.AuthService.controllers;

import com.Library.AuthService.dto.ResetPasswordDTO;
import com.Library.AuthService.dto.SendCodeDTO;
import com.Library.AuthService.services.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reset-password")
public class ResetPasswordController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/public/send-code")
    public ResponseEntity<SendCodeDTO> sendCode(@RequestBody SendCodeDTO sendCodeDTO){
        passwordResetService.sendResetCode(sendCodeDTO.getParam());
        return new ResponseEntity<>(sendCodeDTO, HttpStatus.OK);
    }
    @PostMapping("/public/reset")
    public ResponseEntity<HttpStatus> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO){
        passwordResetService.resetPassword(resetPasswordDTO.getParam(),
                resetPasswordDTO.getCode(), resetPasswordDTO.getNewPassword());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
