package com.library.FrontendMicroservice.controllers;

import com.library.FrontendMicroservice.auth.JwtCookieManager;
import com.library.FrontendMicroservice.models.RegisterRequest;
import com.library.FrontendMicroservice.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
    
    private final AuthService authService;
    private final JwtCookieManager cookieManager;

    @GetMapping("/register")
    public String registerPage(@RequestParam(required = false) String error,
                               @RequestParam(required = false) String success,
                               Model model) {

        model.addAttribute("registerRequest", new RegisterRequest());

        if (error != null) {
            model.addAttribute("errorMessage", "Registration failed. Please try again.");
        }

        if (success != null) {
            model.addAttribute("successMessage", "Registration successful! Please login.");
        }

        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest registerRequest,
                           BindingResult bindingResult,
                           @RequestParam(required = false) String redirect,
                           Model model,
                           HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Please correct the errors below");
            return "register";
        }

        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            model.addAttribute("errorMessage", "Passwords do not match");
            return "register";
        }

        try {
            String token = authService.register(registerRequest);

            cookieManager.setJwtCookie(response, token);

            if (redirect != null && !redirect.isEmpty() && !redirect.equals("null")) {
                return "redirect:" + redirect;
            }
            return "redirect:/home?registered=true";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "register";
        }
    }
}
