package com.library.FrontendMicroservice.auth;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.function.Supplier;

@Slf4j
@Component
public class FeignErrorHandler {
    public static String handleRequest(Supplier<String> action, Model model) {
        try {
            return action.get();
        } catch (FeignException.Unauthorized e) {
            return handleUnauthorized(model, e);
        } catch (FeignException.Forbidden e) {
            return handleForbidden(model, e);
        } catch (FeignException.NotFound e) {
            return handleNotFound(model, e);
        } catch (FeignException e) {
            return handleFeignError(model, e);
        } catch (Exception e) {
            return handleGenericError(model, e);
        }
    }

    private static String handleUnauthorized(Model model, FeignException e) {
//        log.debug("Unauthorized: {}", e.getMessage());
        return "redirect:/login";
    }

    private static String handleForbidden(Model model, FeignException e) {
//        log.debug("Forbidden: {}", e.getMessage());
        model.addAttribute("error", "Access denied");
        return "error/403";
    }

    private static String handleNotFound(Model model, FeignException e) {
//        log.debug("Not found: {}", e.getMessage());
        model.addAttribute("error", "Resource not found");
        return "error/404";
    }

    private static String handleFeignError(Model model, FeignException e) {
//        log.error("Feign error {}: {}", e.status(), e.getMessage());
        model.addAttribute("error", "Service temporarily unavailable");
        return "error/500";
    }

    private static String handleGenericError(Model model, Exception e) {
//        log.error("Unexpected error: {}", e.getMessage(), e);
        model.addAttribute("error", "An unexpected error occurred");
        return "error/500";
    }
}
