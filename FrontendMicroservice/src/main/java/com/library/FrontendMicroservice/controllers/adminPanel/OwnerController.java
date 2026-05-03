package com.library.FrontendMicroservice.controllers.adminPanel;

import com.library.FrontendMicroservice.dto.*;
import com.library.FrontendMicroservice.models.Book;
import com.library.FrontendMicroservice.services.DataPreparationService;
import com.library.FrontendMicroservice.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/owner/users")
public class OwnerController {
    private final UserService adminUserService;
    private final DataPreparationService dataPreparationService;
    private static final int PAGE_SIZE = 10;


    @GetMapping
    public String allUsers(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(required = false) String search,
                           Model model) {

        UserPageDto usersPage = adminUserService.getAllUsers(page, PAGE_SIZE, search);

        List<UserDtoWithId> dto = usersPage.getUsers();
        List<UserDtoForOwner> users = getUsers(dto);
        int totalPages = usersPage.getTotalPages();
        long totalUsers = usersPage.getTotalElements();

        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("searchQuery", search);




        return "admin-panel/owner-users";
    }
    @PostMapping("/{id}/make-admin")
    @ResponseBody
    public ResponseEntity<?> makeAdmin(@PathVariable UUID id) {
        try {
            log.info("Making user an ADMIN: {}", id);
            adminUserService.makeAdmin(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error making user admin: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{id}/make-user")
    @ResponseBody
    public ResponseEntity<?> makeUser(@PathVariable UUID id) {
        try {
            log.info("Making user a regular USER: {}", id);
            adminUserService.makeUser(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error making user: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    private List<UserDtoForOwner> getUsers(List<UserDtoWithId> userDto) {
        Map<UUID, UserDtoWithRoleAndID> map = dataPreparationService.findAuthUserData(userDto);
        List<UserDtoForOwner> result = new ArrayList<>();
        for (UserDtoWithId dtoWithRoleAndID : userDto) {
            UserDtoWithRoleAndID dtoWithRole = map.get(dtoWithRoleAndID.getId());
            UserDtoForOwner dto = new UserDtoForOwner();
                dto.setId(dtoWithRole.getId());
                dto.setRole(dtoWithRole.getRole());
                dto.setEmail(dtoWithRoleAndID.getEmail());
                dto.setUsername(dtoWithRoleAndID.getUsername());
                dto.setLibraryCode(dtoWithRoleAndID.getLibraryCode());
                result.add(dto);
        }
        return result;
    }

}
