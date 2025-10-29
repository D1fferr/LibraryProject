package library.com.userservice1.services;

import library.com.userservice1.dtos.UserDTO;
import library.com.userservice1.dtos.UserDTOForChangeProfile;
import library.com.userservice1.dtos.UserDTOForView;
import library.com.userservice1.exceptions.UserNotFoundException;
import library.com.userservice1.models.User;
import library.com.userservice1.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;



    @Transactional
    public void save(UserDTO userDTO){
        userRepository.save(userDTOToUser(userDTO));
    }
    @Transactional
    public void  updateProfile(UUID id, UserDTOForChangeProfile userDTO){
        User user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("User not found"));
        userRepository.save(userDTOForChangeProfileToUser(userDTO, user));

    }
    @Transactional(readOnly = true)
    public UserDTOForView findUser(UUID id){
        User user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("User not found"));
        return userToUserDTOForView(user);
    }
    @Transactional
    public void deleteById(UUID id){
        userRepository.deleteById(id);
    }
    @Transactional(readOnly = true)
    public List<User> findUser(String param){
        List<User> users = userRepository.findUserByEmailOrLibraryCodeOrUsername(param, param, param);
        if (users.isEmpty())
            throw new UserNotFoundException("Users not found");
        return users;
    }

    private UserDTOForView userToUserDTOForView(User user){
        UserDTOForView userDTOForView = new UserDTOForView();
        userDTOForView.setEmail(user.getEmail());
        userDTOForView.setUsername(user.getUsername());
        userDTOForView.setLibraryCode(user.getLibraryCode());
        return userDTOForView;
    }



    private User userDTOForChangeProfileToUser(UserDTOForChangeProfile userDto, User user){
        if (userDto.getEmail()!=null)
            user.setEmail(userDto.getEmail());
        if (userDto.getLibraryCode()!=null)
            user.setLibraryCode(userDto.getLibraryCode());
        return user;
    }

    private User userDTOToUser(UserDTO userDTO){
        User user = new User();
        user.setId(UUID.fromString(userDTO.getId()));
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        userDTO.setLibraryCode(user.getLibraryCode());
        return user;
    }
}
