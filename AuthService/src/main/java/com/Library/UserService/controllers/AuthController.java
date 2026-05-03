package com.Library.UserService.controllers;

import com.Library.UserService.dto.*;
import com.Library.UserService.models.AuthUser;
import com.Library.UserService.security.JWTProvider;
import com.Library.UserService.services.AuthUserService;
import com.Library.UserService.services.CrossServerRequestService;
import com.Library.UserService.services.TokenBlackListService;
import com.Library.UserService.exceptions.UserNotCreatedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final JWTProvider jwtProvider;
    private final TokenBlackListService tokenBlackListService;
    private final AuthUserService authUserService;
    private final CrossServerRequestService crossServerRequestService;
    @PostMapping("/public/registration")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid UserDTO userDTO,
                                             BindingResult bindingResult) {

        checkErrors(bindingResult);
        AuthUser authUser = authUserService.save(userDTO);
        String token = jwtProvider.generatedToken(userDTO.getUsername(), authUser.getId(), "USER");
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Authorization", "Bearer " + token)
                .build();
    }
    @PostMapping("/public/registration/admin")
    public ResponseEntity<HttpStatus> createAsAdmin(@RequestBody @Valid UserDTO userDTO,
                                             BindingResult bindingResult) {

        checkErrors(bindingResult);
        AuthUser authUser = authUserService.saveAsAdmin(userDTO);
        String token = jwtProvider.generatedToken(userDTO.getUsername(), authUser.getId(), "ADMIN");
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Authorization", "Bearer " + token)
                .build();
    }

    @PostMapping("/public/login")
    public ResponseEntity<LoginDTO> login(@RequestBody LoginDTO loginDTO) {
        AuthUser authUser = authUserService.login(loginDTO);
        String token = jwtProvider.generatedToken(loginDTO.getUsername(), authUser.getId(), authUser.getRole());
        return ResponseEntity.status(HttpStatus.OK)
                .header("Authorization", "Bearer " + token).body(loginDTO);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<String> logout(@RequestHeader(name = "Authorization", required = false) String token) {
        token = token.substring(7);
        tokenBlackListService.blacklistToken(token);
        return ResponseEntity.ok("Logged out successfully");
    }
    @PostMapping("/auth/change-credentials/{id}")
    public ResponseEntity<HttpStatus> changeCredentials(@PathVariable UUID id,
                                                        @RequestBody @Valid ChangeCredentialDTO changeCredentialDTO,
                                                        BindingResult bindingResult){
        checkErrors(bindingResult);
        authUserService.updateCredentials(id, changeCredentialDTO);
        return new ResponseEntity<>(HttpStatus.OK);

    }
    @PostMapping("/auth/get-users")
    public ResponseEntity<UserDtoWithRole> changeCredentials(@RequestBody UserDtoWithListIDs dto){

        return ResponseEntity.ok(authUserService.getUsersByIDs(dto));

    }
    @DeleteMapping("/auth/delete/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable UUID id){
        authUserService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PatchMapping("/auth/do-admin/{id}")
    public ResponseEntity<HttpStatus> doAdmin(@PathVariable UUID id){
        authUserService.doAdmin(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }
    @PatchMapping("/auth/do-user/{id}")
    public ResponseEntity<HttpStatus> doUser(@PathVariable UUID id){
        authUserService.doUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void checkErrors(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessage.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage()).append(";");
            }
            log.info("Errors found in entity fields. Errors: '{}'", errors);
            throw new UserNotCreatedException(errorMessage.toString());
        }
    }
}
