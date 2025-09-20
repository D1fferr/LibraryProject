package com.Library.UserService.controllers;

import com.Library.UserService.dto.AuthDTO;
import com.Library.UserService.dto.UserDTO;
import com.Library.UserService.models.User;
import com.Library.UserService.security.JWTProvider;
import com.Library.UserService.services.TokenBlackListService;
import com.Library.UserService.services.UserService;
import com.Library.UserService.util.UserAlreadyExistException;
import com.Library.UserService.util.UserNotCreatedException;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/user")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTProvider jwtProvider;
    private final TokenBlackListService tokenBlackListService;


    public AuthController(AuthenticationManager authenticationManager, UserService userService, JWTProvider jwtProvider, TokenBlackListService tokenBlackListService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.tokenBlackListService = tokenBlackListService;
    }

//    @GetMapping
//    private List<UserDTO> getAllUsers(@RequestParam(value = "page", defaultValue = "0") Integer page,
//                                      @RequestParam(value = "userPerPage", defaultValue = "5") Integer userPerPage,
//                                      @RequestParam(value = "sortBy", defaultValue = "personUsername") String sortBy){
//        List<UserDTO> users = userService.findAll(PageRequest.of(page, userPerPage, Sort.by(sortBy)));
//        if (users.isEmpty())
//            throw new UsersNotFoundException("No users found");
//        return users;
//    }

    //    @GetMapping("/{user_id}")
//    public UserDTO findById(@PathVariable("user_id") int user_id){
//        return userService.findById(user_id).orElseThrow(()-> new UsersNotFoundException("User not found"));
//    }
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
        Optional<User> userOptional = userService.findByUsername(userDTO.getUsername());
        if (userOptional.isPresent())
            throw new UserAlreadyExistException("A user with this username already exist");
        userService.save(userDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginPerson(@RequestBody AuthDTO authDTO) {
        var authToken = new UsernamePasswordAuthenticationToken(
                authDTO.getUsername(), authDTO.getPassword()
        );
        try {
            authenticationManager.authenticate(authToken);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect credentials");
        }
        String role = userService.findRole(authDTO.getUsername());
        String jwtToken = jwtProvider.generatedToken(authDTO.getUsername(), role);
        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(name = "Authorization", required = false) String token) {
        token = token.substring(7);
        tokenBlackListService.blacklistToken(token);
        System.out.println(token);
        return ResponseEntity.ok("Logged out successfully");
    }


//    @DeleteMapping("/{user_id}")
//    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("user_id") int user_id){
//        userService.deleteUser(user_id);
//        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
//    }


}
