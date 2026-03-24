package com.library.FrontendMicroservice.controllers;

import com.library.FrontendMicroservice.auth.JwtCookieManager;
import com.library.FrontendMicroservice.models.AuthRequest;
import com.library.FrontendMicroservice.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;
    private final JwtCookieManager cookieManager;


    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String redirect,
                            @RequestParam(required = false) String error,
                            @RequestParam(required = false) String expired,
                            Model model) {

        model.addAttribute("redirect", redirect);

        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password");
        }
        if (expired != null) {
            model.addAttribute("warningMessage", "Your session has expired. Please login again.");
        }

        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest,
                        @RequestParam(required = false) String redirect,
                        HttpServletResponse response) {

        try {
            String token = authService.login(authRequest);

            cookieManager.setJwtCookie(response, token);


            if (redirect != null && !redirect.isEmpty() && !redirect.equals("null")) {
                return "redirect:" + redirect;
            }

            return "redirect:/home";

        } catch (Exception e) {
            String redirectParam = (redirect != null && !redirect.isEmpty()) ? "&redirect=" + redirect : "";
            return "redirect:/login?error=true" + redirectParam;
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        cookieManager.clearJwtCookie(response);
        return "redirect:/home";
    }

}
