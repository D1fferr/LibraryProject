package com.library.FrontendMicroservice.controllers;

import com.library.FrontendMicroservice.services.PasswordResetService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ResetPasswordController {

    private final PasswordResetService passwordResetService;

    @GetMapping("profile/reset-password")
    public String resetPasswordRequestPage() {
        return "profile/reset-password-request";
    }

    @PostMapping("/reset-password/request")
    @ResponseBody
    public ResponseEntity<?> requestReset(@RequestBody Map<String, String> request,
                                          HttpSession session) {
        try {

            String param = request.get("param");
            System.out.println(param);
            session.setAttribute("resetParam", param);
            passwordResetService.sendResetCode(param);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/reset-password/verify")
    public String resetPasswordVerifyPage(Model model, HttpSession session) {
        String param = (String) session.getAttribute("resetParam");
        if (param == null) {
            return "redirect:/reset-password";
        }
        model.addAttribute("param", param);
        return "profile/reset-password-verify";
    }

    @PostMapping("/reset-password/verify")
    @ResponseBody
    public ResponseEntity<?> verifyAndReset(@RequestBody Map<String, String> request,
                                            HttpSession session) {
        try {
            String param = (String) session.getAttribute("resetParam");
            String code = request.get("code");
            String newPassword = request.get("newPassword");
            System.out.println(param + code + newPassword);
            if (param == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Session expired. Please start over."));
            }

            // Викликаємо сервіс для скидання пароля
            passwordResetService.resetPassword(param, code, newPassword);

            // Очищаємо сесію
            session.removeAttribute("resetParam");

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/reset-password/success")
    public String resetPasswordSuccess() {
        return "profile/reset-password-success";
    }
}