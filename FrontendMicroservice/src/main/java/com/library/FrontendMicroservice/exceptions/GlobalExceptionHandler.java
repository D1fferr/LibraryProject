package com.library.FrontendMicroservice.exceptions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.FrontendMicroservice.auth.JwtCookieManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

        private final JwtCookieManager cookieManager;

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public String handleUnauthorized(HttpClientErrorException.Unauthorized e,
                                     HttpServletRequest request,
                                     HttpServletResponse response,
                                     RedirectAttributes redirectAttributes) {

        cookieManager.clearJwtCookie(response);
        redirectAttributes.addAttribute("redirect", request.getRequestURI());
        redirectAttributes.addAttribute("expired", "true");

        return "redirect:/login";
    }




    @ExceptionHandler(HttpStatusCodeException.class)
    public Object handleHttpError(HttpStatusCodeException e, HttpServletRequest request, Model model) {

        String errorMessage = extractMessage(e.getResponseBodyAsString());

        String requestedWith = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(requestedWith) || request.getHeader("Accept").contains("application/json")) {
            Map<String, Object> body = new HashMap<>();
            body.put("message", errorMessage);
            body.put("status", e.getStatusCode().value());
            return new ResponseEntity<>(body, e.getStatusCode());
        }

        model.addAttribute("error", errorMessage);
        model.addAttribute("status", e.getStatusCode().value());

        return "error/error-page";
    }
    private String extractMessage(String body) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(body);
            if (jsonNode.has("message")) {
                return jsonNode.get("message").asText();
            }
        } catch (Exception ignored) {}
        return "An error occurred with the external service";
    }
}
