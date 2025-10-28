package com.Library.UserService.controllers;

import com.Library.UserService.dto.LoginDTO;
import com.Library.UserService.dto.UserDTO;
import com.Library.UserService.models.AuthUser;
import com.Library.UserService.security.JWTProvider;
import com.Library.UserService.services.AuthUserService;
import com.Library.UserService.services.CrossServerRequestService;
import com.Library.UserService.services.TokenBlackListService;
import com.Library.UserService.util.UserNotCreatedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JWTProvider jwtProvider;
    private final TokenBlackListService tokenBlackListService;
    private final AuthUserService authUserService;
    private final CrossServerRequestService crossServerRequestService;

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid UserDTO userDTO,
                                             BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessage.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage()).append(";");
            }
            throw new UserNotCreatedException(errorMessage.toString());
        }

        authUserService.save(userDTO);
        AuthUser authUser = authUserService.findByUsername(userDTO.getUsername());
        crossServerRequestService.send(userDTO, authUser.getId());
        String token = jwtProvider.generatedToken(userDTO.getUsername(), authUser.getId(), "USER");

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Authorization", "Bearer " + token)
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<HttpStatus> login (@RequestBody LoginDTO loginDTO) {
        AuthUser authUser = authUserService.login(loginDTO);
        String token = jwtProvider.generatedToken(loginDTO.getUsername(), authUser.getId(), "USER");
        return ResponseEntity.status(HttpStatus.OK)
                .header("Authorization", "Bearer " + token)
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(name = "Authorization", required = false) String token) {
        token = token.substring(7);
        tokenBlackListService.blacklistToken(token);
        return ResponseEntity.ok("Logged out successfully");
    }

}
