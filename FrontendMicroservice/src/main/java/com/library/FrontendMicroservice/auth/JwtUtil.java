package com.library.FrontendMicroservice.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Base64;

@Service
public class JwtUtil {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String JWT_COOKIE_NAME = "jwt-token";

    public String getCurrentUserId() {
        String token = getJwtFromCookie();
        if (token == null) {
            return null;
        }

        try {
            String[] chunks = token.split("\\.");
            if (chunks.length < 2) {
                return null;
            }

            String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));

            JsonNode jsonNode = objectMapper.readTree(payload);

            if (jsonNode.has("user_id")) {
                return jsonNode.get("user_id").asText();
            }
         return null;

        } catch (Exception e) {
            return null;
        }
    }
    public String getCurrentUsername() {
        String token = getJwtFromCookie();
        if (token == null) {
            return null;
        }

        try {
            String[] chunks = token.split("\\.");
            if (chunks.length < 2) {
                return null;
            }

            String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));

            JsonNode jsonNode = objectMapper.readTree(payload);

            if (jsonNode.has("username")) {
                return jsonNode.get("username").asText();
            }
            return null;

        } catch (Exception e) {
            return null;
        }
    }
    public String getCurrentRole() {
        String token = getJwtFromCookie();
        if (token == null) {
            return null;
        }

        try {
            String[] chunks = token.split("\\.");
            if (chunks.length < 2) {
                return null;
            }

            String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));

            JsonNode jsonNode = objectMapper.readTree(payload);

            if (jsonNode.has("role")) {
                return jsonNode.get("role").asText();
            }
            return null;

        } catch (Exception e) {
            return null;
        }
    }

    private String getJwtFromCookie() {
        ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            Cookie[] cookies = request.getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (JWT_COOKIE_NAME.equals(cookie.getName())) {
                        return cookie.getValue();
                    }
                }
            }
        }
        return null;
    }

    public boolean isAuthenticated() {
        return getJwtFromCookie() != null;
    }
}