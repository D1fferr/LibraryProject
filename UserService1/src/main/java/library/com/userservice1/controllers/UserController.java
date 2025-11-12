package library.com.userservice1.controllers;

import jakarta.validation.Valid;
import library.com.userservice1.dtos.ChangeCredentialDTO;
import library.com.userservice1.dtos.UserDTO;
import library.com.userservice1.dtos.UserDTOForChangeProfile;
import library.com.userservice1.dtos.UserDTOForView;
import library.com.userservice1.exceptions.UserNotChangedException;
import library.com.userservice1.exceptions.UserNotCreatedException;
import library.com.userservice1.models.User;
import library.com.userservice1.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;


    @PostMapping("/create")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid UserDTO userDTO,
                                             BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessage.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage()).append(";");
            }
            throw new UserNotCreatedException(errorMessage.toString());
        }
        userService.save(userDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PatchMapping("/change-profile/{id}")
    public ResponseEntity<UserDTOForChangeProfile> changeProfile(@PathVariable UUID id,
                                                                 @RequestBody UserDTOForChangeProfile userDTO){
        userService.updateProfile(id, userDTO);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
    @PatchMapping("/change-credentials/{id}")
    public ResponseEntity<HttpStatus> changeCredential(@PathVariable UUID id,
                                                       @RequestBody ChangeCredentialDTO changeCredentialDTO){

        userService.updateCredential(id, changeCredentialDTO);
        return new ResponseEntity<>(HttpStatus.OK);

    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDTOForView> getOne(@PathVariable UUID id){
        return new ResponseEntity<>(userService.findUser(id), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable UUID id){
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/get-user-by/{param}")
    public ResponseEntity<List<User>> getUserBy(@PathVariable String param){
        return new ResponseEntity<>(userService.findUser(param), HttpStatus.OK);
    }
}
