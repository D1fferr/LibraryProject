package com.library.FrontendMicroservice.controllers;

import com.library.FrontendMicroservice.auth.JwtCookieManager;
import com.library.FrontendMicroservice.auth.JwtUtil;
import com.library.FrontendMicroservice.dto.ChangeCredentialDTO;
import com.library.FrontendMicroservice.dto.UserDTOForView;
import com.library.FrontendMicroservice.models.AuthRequest;
import com.library.FrontendMicroservice.services.AuthService;
import com.library.FrontendMicroservice.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final JwtUtil jwtUtil;
    private final JwtCookieManager jwtCookieManager;
    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/edit-credentials")
    public String editCredentialsPage(Model model) {
        try {
            UUID currentUserId = UUID.fromString(jwtUtil.getCurrentUserId());
            UserDTOForView userDTOForView = userService.getUserById(currentUserId);

            model.addAttribute("currentUsername", userDTOForView.getUsername());
            model.addAttribute("currentEmail", userDTOForView.getEmail());
            model.addAttribute("currentLibraryCode", userDTOForView.getLibraryCode());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Unable to load page");
        }

        return "profile/edit-credentials";
    }

    @PostMapping("/edit-credentials")
    public String updateCredentials(@RequestBody ChangeCredentialDTO request,
                                    HttpServletResponse response) {
        try {
            String currentUserId = jwtUtil.getCurrentUserId();
            System.out.println("trying to update");
            System.out.println(request.getCurrentPassword() + " " + request.getUsername() + " " + request.getPassword());
            userService.updateCredentials(currentUserId, request);
            System.out.println("updated cred");
            authService.logout();
            System.out.println("logout");
            jwtCookieManager.clearJwtCookie(response);
            return "redirect:/login";

        } catch (Exception e) {
            return "redirect:/profile/edit-credentials?error=true";
        }
    }

}
