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


    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleApiErrors(HttpClientErrorException e) {

        String errorMessage = extractMessage(e.getResponseBodyAsString());

        Map<String, Object> body = new HashMap<>();
        body.put("message", errorMessage);
        body.put("status", e.getStatusCode().value());
        body.put("error", e.getStatusText());

        return new ResponseEntity<>(body, e.getStatusCode());
    }



    @ExceptionHandler(HttpServerErrorException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleHttpServerError(HttpServerErrorException e) {

        String errorMessage = extractMessage(e.getResponseBodyAsString());

        Map<String, Object> body = new HashMap<>();
        body.put("message", errorMessage);
        body.put("status", e.getStatusCode().value());
        body.put("error", e.getStatusText());

        return new ResponseEntity<>(body, e.getStatusCode());
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
