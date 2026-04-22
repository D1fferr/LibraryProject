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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

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
    public boolean hasRoleAdmin() {
        try {
            String token = getJwtFromCookie();
            if (token == null) return false;

            String[] chunks = token.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));
            JsonNode jsonNode = objectMapper.readTree(payload);

            if (jsonNode.has("role")) {
                JsonNode roles = jsonNode.get("role");
                return "ADMIN".equals(roles.asText());
            }
            return false;
        } catch (Exception e) {
            return false;
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
        String token = getJwtFromCookie();
        if (token == null) {
            return false;
        }

        LocalDateTime expirationDateTime = getExpirationDateTimeFromToken(token);
        if (expirationDateTime != null && expirationDateTime.isBefore(LocalDateTime.now())) {
            return false;
        }

        return true;
    }

    private LocalDateTime getExpirationDateTimeFromToken(String token) {
        try {
            String[] chunks = token.split("\\.");
            if (chunks.length < 2) {
                return null;
            }

            String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(payload);

            if (jsonNode.has("exp")) {
                long expSeconds = jsonNode.get("exp").asLong();
                // Конвертуємо секунди в LocalDateTime
                return LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(expSeconds),
                        ZoneId.systemDefault()
                );
            }

        } catch (Exception e) {
            System.out.println("Error parsing token: " + e.getMessage());
        }
        return null;
    }
}