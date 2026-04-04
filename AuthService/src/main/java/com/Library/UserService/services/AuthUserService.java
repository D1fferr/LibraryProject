package com.Library.UserService.services;

import com.Library.UserService.dto.ChangeCredentialDTO;
import com.Library.UserService.dto.DoAdminDTO;
import com.Library.UserService.dto.LoginDTO;
import com.Library.UserService.dto.UserDTO;
import com.Library.UserService.models.AuthUser;
import com.Library.UserService.repositories.AuthUserRepository;
import com.Library.UserService.exceptions.UserAlreadyExistException;
import com.Library.UserService.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUserService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final CrossServerRequestService crossServerRequestService;

    @Transactional
    public AuthUser save(UserDTO userDTO){
        Optional<AuthUser> authUser = authUserRepository.findFirstByUsername(userDTO.getUsername());
        if (authUser.isPresent()) {
            log.warn("A user with this username already exists. '{}'", userDTO.getUsername());
            throw new UserAlreadyExistException("A user with this username already exists.");
        }
        AuthUser auth = toAuthUser(userDTO);
        auth.setRole("USER");
        auth.setPassword(passwordEncoder.encode(auth.getPassword()));
        authUserRepository.save(auth);
        crossServerRequestService.sendUser(userDTO, auth.getId());
        log.info("User saved. ID: '{}'", auth.getId());
        return auth;
    }

    @Transactional
    public void saveAfterResetPassword(AuthUser authUser){
        Optional<AuthUser> optionalAuthUser = authUserRepository.findFirstByUsername(authUser.getUsername());
        if (optionalAuthUser.isEmpty()) {
            log.warn("User not found. {}", authUser.getUsername());
            throw new UserNotFoundException("User not found");
        }
        AuthUser auth = optionalAuthUser.get();
        auth.setPassword(passwordEncoder.encode(authUser.getPassword()));
        authUserRepository.save(auth);
        log.info("User saved. ID: '{}'", auth.getId());
    }

    @Transactional
    public AuthUser saveAsAdmin(UserDTO userDTO){
        Optional<AuthUser> authUser = authUserRepository.findFirstByUsername(userDTO.getUsername());
        if (authUser.isPresent()) {
            log.warn("The user not found. Username: '{}'", userDTO.getUsername());
            throw new UserAlreadyExistException("A user with this username already exists.");
        }
        AuthUser auth = toAuthUser(userDTO);
        auth.setRole("ADMIN");
        auth.setPassword(passwordEncoder.encode(auth.getPassword()));
        authUserRepository.save(auth);
        log.info("User saved as admin. ID: '{}'", auth.getId());
        crossServerRequestService.sendUser(userDTO, auth.getId());
        return auth;
    }
    public AuthUser login(LoginDTO loginDTO){
        Optional<AuthUser> optionalAuthUser = authUserRepository.findFirstByUsername(loginDTO.getUsername());
        if (optionalAuthUser.isEmpty()){
            log.warn("The user not found. Username: '{}'", loginDTO.getUsername());
            throw new UserNotFoundException("The user not found");
        }
        AuthUser authUser = optionalAuthUser.get();
        if (!passwordEncoder.matches(loginDTO.getPassword(), authUser.getPassword())){
            log.info("Incorrect password. Username: '{}'", loginDTO.getUsername());
            throw new BadCredentialsException("Incorrect password");
        }
        log.info("Login succeed. Username: '{}'", loginDTO.getUsername());
        return authUser;
    }
    @Transactional
    public void updateCredentials(UUID id, ChangeCredentialDTO changeCredentialDTO){
        log.info("Trying to find the user to update credentials. ID: '{}'", id);
        Optional<AuthUser> optionalAuthUser = authUserRepository.findById(id);
        if (optionalAuthUser.isEmpty()){
            log.warn("The user not found. ID: '{}'", id);
            throw new UserNotFoundException("The user not found");
        }
         AuthUser authUser = optionalAuthUser.get();
         if (changeCredentialDTO.getUsername()!=null)
            authUser.setUsername(changeCredentialDTO.getUsername());
         if (changeCredentialDTO.getPassword()!=null)
            authUser.setPassword(passwordEncoder.encode(changeCredentialDTO.getPassword()));
         if (changeCredentialDTO.getEmail()!=null)
            authUser.setEmail(changeCredentialDTO.getEmail());
         authUserRepository.save(authUser);
         log.info("The user credentials updated. ID: '{}'", id);
         crossServerRequestService.sendCredential(authUser, id);
    }
    @Transactional(readOnly = true)
    public Optional<AuthUser> findByParam(String param){
        return authUserRepository.findByUsernameOrEmail(param, param);
    }
    @Transactional
    public void deleteById(UUID id){
        crossServerRequestService.delete(id);
        authUserRepository.deleteById(id);
        log.info("The user deleted. ID: '{}'", id);
    }
    @Transactional
    public void doAdmin(DoAdminDTO dto){
        log.info("Trying to find user to make him admin. ID: '{}'", dto.getId());
        Optional<AuthUser> optionalAuthUser = authUserRepository.findById(dto.getId());
        if (optionalAuthUser.isEmpty()){
            log.warn("The user not found. ID: '{}'", dto.getId());
            throw new UserNotFoundException("User not found");
        }
        AuthUser authUser = optionalAuthUser.get();
        authUser.setRole("ADMIN");
        authUserRepository.save(authUser);
        log.info("The user saved. ID: '{}'", authUser.getId());
    }

    private AuthUser toAuthUser(UserDTO userDTO){
        log.info("Mapping userDTO to an auth user entity");
        AuthUser authUser = new AuthUser();
        authUser.setUsername(userDTO.getUsername());
        authUser.setPassword(userDTO.getUserPassword());
        authUser.setEmail(userDTO.getUserEmail());
        return authUser;
    }
}
