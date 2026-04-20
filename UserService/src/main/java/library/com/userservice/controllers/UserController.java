package library.com.userservice.controllers;

import jakarta.validation.Valid;
import library.com.userservice.dtos.*;
import library.com.userservice.exceptions.UserNotChangedException;
import library.com.userservice.exceptions.UserNotCreatedException;
import library.com.userservice.models.User;
import library.com.userservice.services.UserService;
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
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
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
            log.info("Errors found in entity fields. Errors: '{}'", errors);
            throw new UserNotCreatedException(errorMessage.toString());
        }
        userService.save(userDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PutMapping("/change-profile/{id}")
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
    @PostMapping("/for-reservations")
    public ResponseEntity<UserDtoWithListUsers> getOne(@RequestBody UserDtoForReservations dto,
                                                       @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                       @RequestParam(value = "usersForPage", defaultValue = "5", required = false) Integer usersForPage){
        return new ResponseEntity<>(userService.findUsersByIds(dto.getUserDTOForViews(), page, usersForPage), HttpStatus.OK);
    }
    @GetMapping("/get-all")
    public ResponseEntity<UserPageDto> getUserBy(@RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "pageSize", defaultValue = "0") int pageSize,
                                                @RequestParam(required = false) String search){

        if (search!=null){
            return new ResponseEntity<>(userService.findUser(page, pageSize, search), HttpStatus.OK);
        }
        return new ResponseEntity<>(userService.findUser(page, pageSize), HttpStatus.OK);
    }
}