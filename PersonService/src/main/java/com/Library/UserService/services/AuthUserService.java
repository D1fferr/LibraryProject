package com.Library.UserService.services;

import com.Library.UserService.dto.LoginDTO;
import com.Library.UserService.dto.UserDTO;
import com.Library.UserService.models.AuthUser;
import com.Library.UserService.repositories.AuthUserRepository;
import com.Library.UserService.util.UserAlreadyExistException;
import com.Library.UserService.util.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final CrossServerRequestService crossServerRequestService;

    @Transactional
    public AuthUser save(UserDTO userDTO){
        Optional<AuthUser> authUser = authUserRepository.findFirstByUsername(userDTO.getUsername());
        if (authUser.isPresent())
            throw new UserAlreadyExistException("A user with this username already exists.");

        AuthUser auth = toAuthUser(userDTO);
        auth.setPassword(passwordEncoder.encode(auth.getPassword()));
        authUserRepository.save(auth);
        crossServerRequestService.send(userDTO, auth.getId());
        return auth;
    }
    public AuthUser login(LoginDTO loginDTO){
        AuthUser authUser = authUserRepository.findFirstByUsername(loginDTO.getUsername())
                .orElseThrow(()->new UserNotFoundException("User not found"));
        if (!passwordEncoder.matches(loginDTO.getPassword(), authUser.getPassword()))
            throw new BadCredentialsException("Incorrect password");
        return authUser;
    }
    @Transactional(readOnly = true)
    public void updateCredentials(UUID id, LoginDTO loginDTO){
         AuthUser authUser = authUserRepository.findById(id)
                 .orElseThrow(()->new UserNotFoundException("User not found"));
         authUser.setUsername(loginDTO.getUsername());
         authUser.setPassword(passwordEncoder.encode(loginDTO.getPassword()));
    }
    @Transactional(readOnly = true)
    public Optional<AuthUser> findByParam(String param){
        return authUserRepository.findByUsernameOrEmail(param, param);
    }
    @Transactional
    public void deleteById(UUID id){
        crossServerRequestService.delete(id);
        authUserRepository.deleteById(id);
    }

    private AuthUser toAuthUser(UserDTO userDTO){
        AuthUser authUser = new AuthUser();
        authUser.setUsername(userDTO.getUsername());
        authUser.setPassword(userDTO.getUserPassword());
        authUser.setEmail(userDTO.getUserEmail());
        authUser.setRole("USER");
        return authUser;
    }
}
