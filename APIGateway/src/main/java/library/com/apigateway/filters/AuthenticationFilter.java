package library.com.apigateway.filters;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;
    private static final Set<String> PUBLIC_ROUTES = Set.of(
            "/api/auth/login",
            "/api/auth/registration",
            "/api/book",
            "/api/book/recently-added-at",
            "/api/reset-password/",
            "/api/user/create",
            "/api/book/most-popular-books",
            "/api/book/category",
            "/api/expected-book/get-all",
            "/api/expected-book"



    );

    private Map<String, Set<String>> roleRequirements() {
        Map<String, Set<String>> role = new HashMap<>();

        // catalog service
        role.put("/book/auth/create", Set.of("ADMIN", "OWNER"));
        role.put("/book/auth/delete/", Set.of("ADMIN", "OWNER"));
        role.put("/book/auth/change-book/", Set.of("ADMIN", "OWNER"));

        // expected book service
        role.put("/expected-book/auth/create", Set.of("ADMIN", "OWNER"));
        role.put("/expected-book/auth/change/", Set.of("ADMIN", "OWNER"));
        role.put("/expected-book/auth/delete/", Set.of("ADMIN", "OWNER"));
        role.put("/expected-book/auth/add-to-current-books/", Set.of("ADMIN", "OWNER"));
        role.put("/report-availability/auth/add", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/report-availability/auth/delete/", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/report-availability-error/auth/get-all", Set.of("ADMIN", "OWNER"));
        role.put("/report-availability-error/auth/send", Set.of("ADMIN", "OWNER"));

        //event service
        role.put("/announcement/auth/create", Set.of("ADMIN", "OWNER"));
        role.put("/announcement/auth/change/", Set.of("ADMIN", "OWNER"));
        role.put("/announcement/auth/delete/", Set.of("ADMIN", "OWNER"));
        role.put("/news/auth/create", Set.of("ADMIN", "OWNER"));
        role.put("/news/auth/change", Set.of("ADMIN", "OWNER"));
        role.put("/news/auth/delete/", Set.of("ADMIN", "OWNER"));

        // order service
        role.put("/reservation/auth/view_for_the_user/", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/reservation/auth/view_for_the_book/", Set.of("ADMIN", "OWNER"));
        role.put("/reservation/auth/view_all", Set.of("ADMIN", "OWNER"));
        role.put("/reservation/auth/view/", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/reservation/auth/create", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/reservation/auth/change_date/", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/reservation/auth/change_status/", Set.of("ADMIN", "OWNER"));
        role.put("/reservation/auth/delete/", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/cancel-reservation/cancel/", Set.of("ADMIN", "OWNER"));
        role.put("/cancel-reservation/user/", Set.of("ADMIN", "OWNER"));

        //auth service
        role.put("/auth/logout", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/auth/change-credentials/", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/auth/delete/", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/auth/do-admin", Set.of("ADMIN", "USER", "OWNER"));

        //user service
        role.put("/user/change-profile/", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/user/change-credentials/", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/user/", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/user/delete/", Set.of("ADMIN", "USER", "OWNER"));
        role.put("/user/get-user-by/", Set.of("ADMIN", "OWNER"));



        return role;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        log.info("Starting of the filter");
        if (isPublicRoute(path)) {
            log.info("The route is public. Path: '{}'", path);
            return chain.filter(exchange);
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
        if (!hasRequiredRole(token, path)) {
            log.info("The role is forbidden for this route");
            return forbidden(exchange, "Insufficient permissions");
        }
        log.info("The filter is completed");
        return chain.filter(exchange.mutate().request(request).build());
    }

    private boolean isPublicRoute(String path) {
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
        String requirePath = roleRequirements().keySet().stream()
                .filter(path::startsWith)
                .findFirst()
                .orElse(null);
        if (requirePath == null)
            return true;

        Set<String> requiredRoles = roleRequirements().get(requirePath);
        List<String> userRoles = jwtUtil.extractRoles(token);

        return userRoles.stream().anyMatch(requiredRoles::contains);
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
