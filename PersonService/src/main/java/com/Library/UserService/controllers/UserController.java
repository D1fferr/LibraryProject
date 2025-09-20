package com.Library.UserService.controllers;

import com.Library.UserService.dto.AuthDTO;
import com.Library.UserService.dto.UserDTO;
import com.Library.UserService.models.User;
import com.Library.UserService.security.JWTProvider;
import com.Library.UserService.services.TokenBlackListService;
import com.Library.UserService.services.UserService;
import com.Library.UserService.util.UserAlreadyExistException;
import com.Library.UserService.util.UserNotChangeException;
import com.Library.UserService.util.UserNotCreatedException;
import com.Library.UserService.util.UsersNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    private List<UserDTO> getAllUsers(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "userPerPage", defaultValue = "5") Integer userPerPage,
                                      @RequestParam(value = "sortBy", defaultValue = "personUsername") String sortBy){
        List<UserDTO> users = userService.findAll(PageRequest.of(page, userPerPage, Sort.by(sortBy)));
        if (users.isEmpty())
            throw new UsersNotFoundException("No users found");
        return users;
    }
    @PatchMapping("/change/{user_id}")
    public ResponseEntity<HttpStatus> changeUser(@PathVariable UUID user_id,
                                                 @RequestBody @Valid UserDTO userDTO,
                                             BindingResult bindingResult) {
        checkErrors(bindingResult);
        userService.update(user_id, userDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }@PatchMapping("/changeLibraryCode/{user_id}")
    public ResponseEntity<HttpStatus> changeUserLibraryCode(@PathVariable UUID user_id,
                                                            @RequestBody @Valid UserDTO userDTO,
                                             BindingResult bindingResult) {
        checkErrors(bindingResult);
        userService.updateLibraryCode(user_id, userDTO);
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
            throw new UserNotChangeException(errorMessage.toString());
        }
    }

    @GetMapping("/{user_id}")
    public UserDTO findById(@PathVariable("user_id") UUID user_id){
        return userService.findById(user_id).orElseThrow(()-> new UsersNotFoundException("User not found"));
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable UUID user_id){
        userService.deleteUser(user_id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }


}
