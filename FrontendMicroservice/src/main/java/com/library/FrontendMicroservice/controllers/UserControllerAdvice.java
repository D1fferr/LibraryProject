package com.library.FrontendMicroservice.controllers;

import com.library.FrontendMicroservice.auth.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class UserControllerAdvice {

    private final JwtUtil jwtUtil;

    @ModelAttribute("isAuthenticated")
    public boolean isAuthenticated() {
        return jwtUtil.isAuthenticated();
    }

    @ModelAttribute("currentUsername")
    public String getCurrentUsername() {
        if(jwtUtil.isAuthenticated()){
            return jwtUtil.getCurrentUsername();
        }
        return "Guest";
    }
    @ModelAttribute("hasRoleAdmin")
    public boolean hasRoleAdmin() {
        try {
            return jwtUtil.hasRoleAdmin();
        } catch (Exception e) {
            return false;
        }
    }
}