package com.Library.UserService.services;

import com.Library.UserService.dto.UserDTO;
import com.Library.UserService.models.User;
import com.Library.UserService.repositories.UserRepository;
import com.Library.UserService.util.UsersNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional(readOnly = true)
    public List<UserDTO> findAll(Pageable pageable){
        return userRepository.findAll(pageable).stream().map(this::toDTO).toList();
    }
    @Transactional(readOnly = true)
    public Optional<UserDTO> findById(UUID id){
        return userRepository.findByUserId((id)).map(this::toDTO);
    }
    @Transactional
    public void deleteUser(UUID id){
        userRepository.deleteByUserId(id);
    }
    @Transactional
    public void save(UserDTO userDTO){
        User user = toEntity(userDTO);
        System.out.println(user);
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        user.setUserRole("ROLE_USER");
        System.out.println(user);
        userRepository.save(user);
    }
    @Transactional
    public void update(UUID id, UserDTO userDTO){
        User user = userRepository.findByUserId(id)
                .orElseThrow(()->new UsersNotFoundException("User not found"));
        if (userDTO.getUsername()!=null)
            user.setUsername(userDTO.getUsername());
        if (userDTO.getUserEmail() != null)
            user.setUserEmail(userDTO.getUserEmail());
        if (userDTO.getUserPassword() != null)
            user.setUserPassword(passwordEncoder.encode(userDTO.getUserPassword()));
        userRepository.save(user);
    }
    @Transactional
    public void updateLibraryCode(UUID id, UserDTO userDTO){
        User user = userRepository.findByUserId(id)
                .orElseThrow(()->new UsersNotFoundException("User not found"));
        if (userDTO.getUserLibraryCode() != null)
            user.setUserLibraryCode(userDTO.getUserLibraryCode());
        userRepository.save(user);
    }
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }
    @Transactional(readOnly = true)
    public String findRole(String username){
       return userRepository.findRole(username);
    }



    private User toEntity(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }
    private UserDTO toDTO(User user){
        return modelMapper.map(user, UserDTO.class);
    }
}
