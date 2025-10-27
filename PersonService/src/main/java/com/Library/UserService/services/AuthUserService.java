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

@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final AuthUserRepository authUserRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void save(UserDTO userDTO){
        Optional<AuthUser> authUser = authUserRepository.findFirstByUsername(userDTO.getUsername());
        if (authUser.isEmpty())
            throw new UserAlreadyExistException("A user with this username already exists.");
        AuthUser auth = toAuthUser(userDTO);
        auth.setPassword(passwordEncoder.encode(auth.getPassword()));
        authUserRepository.save(auth);
    }
    @Transactional(readOnly = true)
    public AuthUser findByUsername(String username){
        return authUserRepository.findFirstByUsername(username).orElseThrow(()->new UserNotFoundException("User not found"));
    }
    public AuthUser login(LoginDTO loginDTO){
        AuthUser authUser = authUserRepository.findFirstByUsername(loginDTO.getUsername())
                .orElseThrow(()->new UserNotFoundException("User not found"));
        if (!passwordEncoder.matches(loginDTO.getPassword(), authUser.getPassword()))
            throw new BadCredentialsException("Incorrect password");
        return authUser;
    }

    private AuthUser toAuthUser(UserDTO userDTO){
        AuthUser authUser = new AuthUser();
        authUser.setUsername(userDTO.getUsername());
        authUser.setPassword(userDTO.getUserPassword());
        authUser.setRole("USER");
        return authUser;
    }
}
