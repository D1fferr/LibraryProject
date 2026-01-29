package library.com.userservice1;

import library.com.userservice1.dtos.UserDTO;
import library.com.userservice1.dtos.UserDTOForChangeProfile;
import library.com.userservice1.dtos.UserDTOForView;
import library.com.userservice1.exceptions.UserNotFoundException;
import library.com.userservice1.models.User;
import library.com.userservice1.repositories.UserRepository;
import library.com.userservice1.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testSave_ValidUserDTO_UserSavedSuccessfully() {

        UUID id = UUID.randomUUID();
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
        userDTO.setLibraryCode("1234");
        userDTO.setId(id.toString());


        User expectedUser = new User();
        expectedUser.setUsername("testuser");
        expectedUser.setEmail("test@example.com");
        expectedUser.setLibraryCode("1234");
        expectedUser.setId(id);

        when(userRepository.save(any(User.class))).thenReturn(expectedUser);
        userService.save(userDTO);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateProfile_UserExists_ShouldUpdateSuccessfully() {

        UUID id = UUID.randomUUID();
        UserDTOForChangeProfile userDTO = new UserDTOForChangeProfile();
        userDTO.setLibraryCode("1234");

        User existingUser = new User();
        existingUser.setUsername("testuser");
        existingUser.setEmail("test@example.com");
        existingUser.setLibraryCode("12346");
        existingUser.setId(id);

        User updatedUser = existingUser;
        updatedUser.setLibraryCode("1234");

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        userService.updateProfile(id, userDTO);
        verify(userRepository, times(1)).findById(id);


        verify(userRepository, times(1)).save(updatedUser);
    }
    @Test
    void updateProfile_UserNotFound_ShouldThrowUserNotFoundException() {

        UUID nonExistentUserId = UUID.randomUUID();
        UserDTOForChangeProfile userDTO = new UserDTOForChangeProfile();
        userDTO.setLibraryCode("1234");
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.updateProfile(nonExistentUserId, userDTO)
        );
        assertEquals("User not found", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
        verify(userRepository, times(1)).findById(nonExistentUserId);
    }

    @Test
    void findUser_UserExists_ShouldReturnUserDTO() {
        UUID id = UUID.randomUUID();

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setLibraryCode("1234");
        user.setId(id);

        UserDTOForView expectedDTO = new UserDTOForView();
        expectedDTO.setLibraryCode("123456");
        expectedDTO.setUsername("JohnDoe");
        expectedDTO.setEmail("john.doe@example.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDTOForView result = userService.findUser(id);

        verify(userRepository, times(1)).findById(id);

        assertNotNull(result);

    }
    @Test
    void findUser_UserNotFound_ShouldThrowUserNotFoundException() {
        UUID nonExistentUserId = UUID.randomUUID();

        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.findUser(nonExistentUserId)
        );

        assertEquals("User not found", exception.getMessage());

        verify(userRepository, times(1)).findById(nonExistentUserId);
    }



}