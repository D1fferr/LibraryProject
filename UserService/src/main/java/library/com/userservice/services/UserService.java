package library.com.userservice.services;

import library.com.userservice.dtos.*;
import library.com.userservice.exceptions.UserNotFoundException;
import library.com.userservice.models.User;
import library.com.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void save(UserDTO userDTO){
        User user = userDTOToUser(userDTO);
        userRepository.save(user);
        log.info("User saved. ID: '{}'", user.getId());
    }
    @Transactional
    public void  updateProfile(UUID id, UserDTOForChangeProfile userDTO){
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()){
            log.warn("The user not found. ID: '{}'", id);
            throw new UserNotFoundException("User not found");
        }

        userRepository.save(userDTOForChangeProfileToUser(userDTO, optionalUser.get()));
        log.info("Profile updated. ID: '{}'", id);
    }
    @Transactional(readOnly = true)
    public UserDTOForView findUser(UUID id){
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()){
            log.warn("The user not found. ID: '{}'", id);
            throw new UserNotFoundException("User not found");
        }
        log.info("The user found. ID: '{}'", id);
        return userToUserDTOForView(optionalUser.get());
    }
    @Transactional
    public void deleteById(UUID id){
        userRepository.deleteById(id);
        log.info("The user deleted. ID: '{}'", id);
    }
    @Transactional(readOnly = true)
    public UserPageDto findUser(int page, int pageSize, String search){
        String decodedSearch = URLDecoder.decode(search, StandardCharsets.UTF_8);
        String searchPattern = "%" + decodedSearch.trim().replaceAll("\\s+", "%") + "%";
        Page<User> users = userRepository.searchEverywhere(searchPattern, PageRequest.of(page, pageSize));
        if (users.isEmpty()) {
            log.warn("Users not found");
            throw new UserNotFoundException("Users not found");
        }
        UserPageDto dto = new UserPageDto();
        dto.setUsers(users.stream().map(this::toUserDtoWithId).toList());
        dto.setTotalPages(users.getTotalPages());
        dto.setTotalElements(users.getTotalElements());
        return dto;
    }
    @Transactional(readOnly = true)
    public UserPageDto findUser(int page, int pageSize){
        Page<User> users = userRepository.findAll(PageRequest.of(page, pageSize));
        if (users.isEmpty()) {
            log.warn("Users not found");
            throw new UserNotFoundException("Users not found");
        }
        UserPageDto dto = new UserPageDto();
        dto.setUsers(users.stream().map(this::toUserDtoWithId).toList());
        dto.setTotalPages(users.getTotalPages());
        dto.setTotalElements(users.getTotalElements());
        return dto;
    }
    @Transactional
    public void updateCredential(UUID id, ChangeCredentialDTO changeCredentialDTO){
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()){
            log.warn("The user not found. ID: '{}'", id);
            throw new UserNotFoundException("The user not found");
        }
        User user = optionalUser.get();
        if (changeCredentialDTO.getUsername()!=null)
            user.setUsername(changeCredentialDTO.getUsername());
        if (changeCredentialDTO.getEmail()!=null)
            user.setEmail(changeCredentialDTO.getEmail());
        if (changeCredentialDTO.getLibraryCode()!=null)
            user.setLibraryCode(changeCredentialDTO.getLibraryCode());
        userRepository.save(user);
        log.info("The user updated. ID: '{}'", id);
    }

    public UserDtoWithListUsers findUsersByIds(List<UUID> uuids, int page, int usersPerPage){
        Page<User> dto = userRepository.findAllByIdIn(uuids, PageRequest.of(page, usersPerPage));
        if (dto.isEmpty()) {
            log.warn("Users not found");
            throw new UserNotFoundException("Users not found");
        }
        UserDtoWithListUsers users = new UserDtoWithListUsers();
        users.setUserDTOForViewList(dto.getContent().stream().map(this::toUserDtoWithId).toList());
        return users;
    }

    private UserDTOForView userToUserDTOForView(User user){
        log.info("Mapping the user entity to userDTO for view");
        UserDTOForView userDTOForView = new UserDTOForView();
        userDTOForView.setEmail(user.getEmail());
        userDTOForView.setUsername(user.getUsername());
        userDTOForView.setLibraryCode(user.getLibraryCode());
        return userDTOForView;
    }



    private User userDTOForChangeProfileToUser(UserDTOForChangeProfile userDto, User user){
        log.info("Mapping the userDTO for changing profile to entity. ID: '{}'", user.getId());
        if (userDto.getLibraryCode()!=null)
            user.setLibraryCode(userDto.getLibraryCode());
        return user;
    }

    private User userDTOToUser(UserDTO userDTO){
        log.info("Mapping the userDTO to entity");
        User user = new User();
        user.setId(UUID.fromString(userDTO.getId()));
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setLibraryCode(userDTO.getLibraryCode());
        return user;
    }
    private UserDtoWithId toUserDtoWithId(User user){
        UserDtoWithId dto = new UserDtoWithId();
        dto.setId(user.getId());
        dto.setLibraryCode(user.getLibraryCode());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }
}