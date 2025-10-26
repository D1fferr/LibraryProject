package com.Library.UserService.services;

import com.Library.UserService.dto.AuthDTO;
import com.Library.UserService.dto.UserDTO;
import com.Library.UserService.models.AuthUser;
import com.Library.UserService.models.User;
import com.Library.UserService.repositories.AuthUserRepository;
import com.Library.UserService.util.UserAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final AuthUserRepository authUserRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public AuthUser save(UserDTO userDTO){
        AuthUser  authUser = authUserRepository.findFirstByUsername(userDTO.getUsername())
                .orElseThrow(()->new UserAlreadyExistException("A user with this username already exists."));
        authUserRepository.save(toAuthUser(userDTO));
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
