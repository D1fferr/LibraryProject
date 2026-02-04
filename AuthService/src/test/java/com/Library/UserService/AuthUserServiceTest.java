package com.Library.UserService;

import com.Library.UserService.dto.ChangeCredentialDTO;
import com.Library.UserService.dto.LoginDTO;
import com.Library.UserService.dto.UserDTO;
import com.Library.UserService.exceptions.UserAlreadyExistException;
import com.Library.UserService.exceptions.UserNotFoundException;
import com.Library.UserService.models.AuthUser;
import com.Library.UserService.repositories.AuthUserRepository;
import com.Library.UserService.services.AuthUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthUserServiceTest {

    @Mock
    private AuthUserRepository authUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthUserService userService;

    @Test
    void save_UserDoesNotExist_ShouldSave() {
        UUID id = UUID.randomUUID();
        UserDTO userDTO = new UserDTO();
        userDTO.setUserEmail("test@email.com");
        userDTO.setUserPassword("PasswordTest");
        userDTO.setUsername("TestUsername");
        userDTO.setUserLibraryCode("1234");

        when(authUserRepository.findFirstByUsername("TestUsername")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("PasswordTest")).thenReturn("encodedpassword");

        AuthUser savedUser = new AuthUser();
        savedUser.setId(id);
        savedUser.setUsername("TestUsername");
        when(authUserRepository.save(any(AuthUser.class))).thenReturn(savedUser);

        AuthUser result = userService.save(userDTO);

        verify(authUserRepository).findFirstByUsername("TestUsername");
        verify(passwordEncoder).encode("PasswordTest");
        verify(authUserRepository).save(any(AuthUser.class));
        assertNotNull(result);
    }

    @Test
    void save_UserAlreadyExists_ShouldThrowException() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("existinguser");

        AuthUser existingUser = new AuthUser();
        when(authUserRepository.findFirstByUsername("existinguser")).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(UserAlreadyExistException.class, () -> {
            userService.save(userDTO);
        });

        verify(authUserRepository).findFirstByUsername("existinguser");
        verify(authUserRepository, never()).save(any());
    }

    @Test
    void save_UserRoleShouldBeSetToUSER() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setUserPassword("password");
        userDTO.setUserEmail("test@email.com");
        userDTO.setUserLibraryCode("1234");

        when(authUserRepository.findFirstByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encoded");

        AuthUser result = userService.save(userDTO);
        assertEquals("USER", result.getRole());
        verify(authUserRepository).save(any(AuthUser.class));
    }

    @Test
    void save_PasswordShouldBeEncoded() {

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setUserPassword("password");
        userDTO.setUserEmail("test@email.com");
        userDTO.setUserLibraryCode("1234");

        when(authUserRepository.findFirstByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encoded_password");

        userService.save(userDTO);

        verify(passwordEncoder).encode("password");
    }
    @Test
    void login_UserExistsPasswordCorrect_ShouldReturnUser() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("user");
        loginDTO.setPassword("pass");

        AuthUser authUser = new AuthUser();
        authUser.setPassword("encodedPass");

        when(authUserRepository.findFirstByUsername("user")).thenReturn(Optional.of(authUser));
        when(passwordEncoder.matches("pass", "encodedPass")).thenReturn(true);

        AuthUser result = userService.login(loginDTO);
        assertSame(authUser, result);
    }

    @Test
    void login_UserNotFound_ShouldThrowException() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("nonexistent");

        when(authUserRepository.findFirstByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.login(loginDTO);
        });
    }

    @Test
    void login_PasswordIncorrect_ShouldThrowException() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("user");
        loginDTO.setPassword("wrong");

        AuthUser authUser = new AuthUser();
        authUser.setPassword("encodedPass");

        when(authUserRepository.findFirstByUsername("user")).thenReturn(Optional.of(authUser));
        when(passwordEncoder.matches("wrong", "encodedPass")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> {
            userService.login(loginDTO);
        });
    }
    @Test
    void updateCredentials_UserExists_ShouldUpdate() {
        UUID userId = UUID.randomUUID();
        ChangeCredentialDTO dto = new ChangeCredentialDTO();
        dto.setUsername("newuser");
        dto.setPassword("newpass");
        dto.setEmail("new@email.com");

        AuthUser authUser = new AuthUser();
        when(authUserRepository.findById(userId)).thenReturn(Optional.of(authUser));
        when(passwordEncoder.encode("newpass")).thenReturn("encodedPass");

        userService.updateCredentials(userId, dto);

        verify(authUserRepository).save(authUser);
    }
    @Test
    void updateCredentials_UserNotFound_ShouldThrowException() {
        UUID userId = UUID.randomUUID();
        ChangeCredentialDTO dto = new ChangeCredentialDTO();

        when(authUserRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.updateCredentials(userId, dto);
        });
    }
    @Test
    void updateCredentials_OnlyUsername_ShouldUpdateOnlyUsername() {
        UUID userId = UUID.randomUUID();
        ChangeCredentialDTO dto = new ChangeCredentialDTO();
        dto.setUsername("newuser");

        AuthUser authUser = new AuthUser();
        when(authUserRepository.findById(userId)).thenReturn(Optional.of(authUser));

        userService.updateCredentials(userId, dto);
        AuthUser authUser1 = authUserRepository.findById(userId).get();
        assertEquals(dto.getUsername(), authUser1.getUsername());
    }

}
