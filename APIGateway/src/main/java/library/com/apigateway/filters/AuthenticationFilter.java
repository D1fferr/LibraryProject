package library.com.apigateway.filters;

import library.com.apigateway.security.JwtBlacklistHandler;
import library.com.apigateway.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;
    private final JwtBlacklistHandler blacklistHandler;
    private static final Set<String> PUBLIC_ROUTES = Set.of(
            "/api/auth/public",
            "/api/book/public",
            "/api/reset-password/public",
            "/api/user/public",
            "/api/expected-book/public",
            "/api/announcement/public",
            "/api/news/public",
            "/api/images/"

    );
    private final Map<String, Set<String>> rolesMap = roleRequirements();

    private Map<String, Set<String>> roleRequirements() {
        Map<String, Set<String>> role = new HashMap<>();

        // catalog service
        role.put("/api/book/auth/create", Set.of("ADMIN", "OWNER"));
        role.put("/api/book/auth/delete/", Set.of("ADMIN", "OWNER"));
        role.put("/api/book/auth/change-book/", Set.of("ADMIN", "OWNER"));
        role.put("/api/book/auth/for-reservations", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/api/book/auth", Set.of("ADMIN", "OWNER"));

        // expected book service
        role.put("/api/expected-book/auth/create", Set.of("ADMIN", "OWNER"));
        role.put("/api/expected-book/auth/change/", Set.of("ADMIN", "OWNER"));
        role.put("/api/expected-book/auth/delete/", Set.of("ADMIN", "OWNER"));
        role.put("/api/expected-book/auth/add-to-current-books/", Set.of("ADMIN", "OWNER"));
        role.put("/api/report-availability/auth/add", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/api/report-availability/auth/delete/", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/api/report-availability-error/auth/get-all", Set.of("ADMIN", "OWNER"));
        role.put("/api/report-availability-error/auth/send", Set.of("ADMIN", "OWNER"));

        //event service
        role.put("/api/announcement/auth/create", Set.of("ADMIN", "OWNER"));
        role.put("/api/announcement/auth/change/", Set.of("ADMIN", "OWNER"));
        role.put("/api/announcement/auth/delete/", Set.of("ADMIN", "OWNER"));
        role.put("/api/news/auth/create", Set.of("ADMIN", "OWNER"));
        role.put("/api/news/auth/change", Set.of("ADMIN", "OWNER"));
        role.put("/api/news/auth/delete/", Set.of("ADMIN", "OWNER"));

        // order service
        role.put("/api/reservation/auth/view_for_the_user/", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/api/reservation/auth/view_for_the_book/", Set.of("ADMIN", "OWNER"));
        role.put("/api/reservation/auth/view_all", Set.of("ADMIN", "OWNER"));
        role.put("/api/reservation/auth/view/", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/api/reservation/auth/create", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/api/reservation/auth/change_date/", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/api/reservation/auth/change_status/", Set.of("ADMIN", "OWNER"));
        role.put("/api/reservation/auth/delete/", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/api/cancel-reservation/auth/cancel/", Set.of("ADMIN", "OWNER"));
        role.put("/api/cancel-reservation/auth/user/", Set.of("ADMIN", "OWNER"));
        role.put("/api/cancel-reservation/auth/user/get-one/", Set.of("ADMIN", "USER", "OWNER"));

        //auth service
        role.put("/api/auth/auth/logout", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/api/auth/auth/change-credentials/", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/api/auth/auth/delete/", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/api/auth//auth/get-users", Set.of("OWNER"));
        role.put("/api/auth/auth/do-admin", Set.of("OWNER"));
        role.put("/api/auth/auth/do-user", Set.of("OWNER"));

        //user service
        role.put("/api/user/auth/change-profile/", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/api/user/auth/change-credentials/", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/api/user/auth/create", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/api/user/auth/", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/api/user/auth/delete/", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/api/user/auth/for-reservations/", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/api/user/auth/get-all/", Set.of("ADMIN", "OWNER"));

        return role;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getAttributes().put(org.springframework.cloud.gateway.support.ServerWebExchangeUtils.PRESERVE_HOST_HEADER_ATTRIBUTE, true);
        ServerHttpRequest request = exchange.getRequest();

        String path = request.getPath().value();
        log.info("Starting of the filter");
        if (isPublicRoute(path)) {
            log.info("The route is public. Path: '{}'", path);
            return chain.filter(exchange);
        }else{
            log.info("The route is not public. Path: '{}'", path);
        }
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            log.info("Missing jwt token for authorized request. Path: '{}'", path);
            return unauthorized(exchange, "Missing JWT token");
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            log.info("Invalid jwt token");
            return unauthorized(exchange, "Invalid JWT token");
        }
        return blacklistHandler.isTokenBlacklisted(token)
                .flatMap(isBlacklisted -> {
                    if (isBlacklisted) {
                        log.info("Token is blacklisted: '{}'", token);
                        return unauthorized(exchange, "Token has been revoked (logout)");
                    }

                    if (!hasRequiredRole(token, path)) {
                        log.info("The role is forbidden for this route");
                        return forbidden(exchange, "Insufficient permissions");
                    }

                    log.info("The filter is completed");
                    return chain.filter(exchange);
                });
    }

    private boolean isPublicRoute(String path) {
        if (!path.startsWith("/api/")) {
            return true;
        }
        return PUBLIC_ROUTES.stream().anyMatch(path::startsWith);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String responseBody = String.format(
                "{\"error\": \"unauthorized\", \"message\": \"%s\", \"code\": \"AUTH_REQUIRED\"}",
                message
        );

        byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
        var buffer = response.bufferFactory().wrap(bytes);

        return response.writeWith(Mono.just(buffer));
    }

    private boolean hasRequiredRole(String token, String path) {
        String requirePath = rolesMap.keySet().stream()
                .filter(path::startsWith)
                .sorted(Comparator.comparingInt(String::length).reversed())
                .findFirst()
                .orElse(null);
        if (requirePath == null) {
            return true;
        }

        Set<String> requiredRoles = rolesMap.get(requirePath);
        String userRoles = jwtUtil.extractRoles(token);
        if (userRoles == null || userRoles.isEmpty()) {
            return false;
        }
        return requiredRoles.stream().anyMatch(userRoles::contains);
    }

    private Mono<Void> forbidden(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String responseBody = String.format(
                "{\"error\": \"forbidden\", \"message\": \"%s\", \"code\": \"INSUFFICIENT_PERMISSIONS\"}",
                message
        );

        byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
        var buffer = response.bufferFactory().wrap(bytes);

        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
