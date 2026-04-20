package com.library.FrontendMicroservice.exceptions;

import com.library.FrontendMicroservice.auth.JwtCookieManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

        private final JwtCookieManager cookieManager;

        // ==================== 401 UNAUTHORIZED ====================
        @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
        public String handleUnauthorized(HttpClientErrorException.Unauthorized e,
                                         HttpServletRequest request,
                                         HttpServletResponse response,
                                         RedirectAttributes redirectAttributes) {



            // Очищаємо прострочений токен
            cookieManager.clearJwtCookie(response);

            redirectAttributes.addAttribute("redirect", request.getRequestURI());
            redirectAttributes.addAttribute("expired", "true");

            return "redirect:/login";
        }

        // ==================== 403 FORBIDDEN ====================
        @ExceptionHandler(HttpClientErrorException.Forbidden.class)
        public String handleForbidden(HttpClientErrorException.Forbidden e,
                                      Model model,
                                      HttpServletRequest request) {


            model.addAttribute("error", "You don't have permission to access this resource");
            model.addAttribute("errorCode", "403");
            model.addAttribute("path", request.getRequestURI());
            model.addAttribute("timestamp", java.time.LocalDateTime.now());

            return "error/403";
        }

        // ==================== 404 NOT FOUND ====================
        @ExceptionHandler(HttpClientErrorException.NotFound.class)
        public String handleNotFound(HttpClientErrorException.NotFound e,
                                     Model model,
                                     HttpServletRequest request) {


            model.addAttribute("error", "The requested resource was not found");
            model.addAttribute("errorCode", "404");
            model.addAttribute("path", request.getRequestURI());
            model.addAttribute("timestamp", java.time.LocalDateTime.now());

            return "error/404";
        }

        // ==================== 400 BAD REQUEST ====================
        @ExceptionHandler(HttpClientErrorException.BadRequest.class)
        public String handleBadRequest(HttpClientErrorException.BadRequest e,
                                       Model model,
                                       HttpServletRequest request) {


            model.addAttribute("error", "Invalid request parameters");
            model.addAttribute("errorCode", "400");
            model.addAttribute("path", request.getRequestURI());
            model.addAttribute("timestamp", java.time.LocalDateTime.now());
            model.addAttribute("details", e.getResponseBodyAsString());

            return "error/400";
        }

        // ==================== 500 INTERNAL SERVER ERROR ====================
        @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
        public String handleInternalServerError(HttpServerErrorException.InternalServerError e,
                                                Model model,
                                                HttpServletRequest request) {


            model.addAttribute("error", "An internal server error occurred");
            model.addAttribute("errorCode", "500");
            model.addAttribute("path", request.getRequestURI());
            model.addAttribute("timestamp", java.time.LocalDateTime.now());

            return "error/500";
        }

        // ==================== 503 SERVICE UNAVAILABLE ====================
        @ExceptionHandler(HttpServerErrorException.ServiceUnavailable.class)
        public String handleServiceUnavailable(HttpServerErrorException.ServiceUnavailable e,
                                               Model model,
                                               HttpServletRequest request) {


            model.addAttribute("error", "Service is temporarily unavailable. Please try again later.");
            model.addAttribute("errorCode", "503");
            model.addAttribute("path", request.getRequestURI());
            model.addAttribute("timestamp", java.time.LocalDateTime.now());

            return "error/503";
        }

        // ==================== CONNECTION TIMEOUT / NETWORK ERRORS ====================
        @ExceptionHandler(ResourceAccessException.class)
        public String handleResourceAccess(ResourceAccessException e,
                                           Model model,
                                           HttpServletRequest request) {


            model.addAttribute("error", "Cannot connect to the service. Please check your connection.");
            model.addAttribute("errorCode", "503");
            model.addAttribute("path", request.getRequestURI());
            model.addAttribute("timestamp", java.time.LocalDateTime.now());

            return "error/503";
        }

        // ==================== ВСІ ІНШІ ПОМИЛКИ ====================
        @ExceptionHandler(Exception.class)
        public String handleGenericError(Exception e,
                                         Model model,
                                         HttpServletRequest request) {


            model.addAttribute("error", "An unexpected error occurred");
            model.addAttribute("errorCode", "500");
            model.addAttribute("path", request.getRequestURI());
            model.addAttribute("timestamp", java.time.LocalDateTime.now());
            model.addAttribute("exception", e.getClass().getSimpleName());

            return "error/500";
        }
}
