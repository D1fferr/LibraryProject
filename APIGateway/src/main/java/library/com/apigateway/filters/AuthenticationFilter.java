package library.com.apigateway.filters;
import library.com.apigateway.security.JwtUtil;
import lombok.RequiredArgsConstructor;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;
    private static final Set<String> PUBLIC_ROUTES = Set.of(
            "auth/login",
            "auth/registration",
            "public/"

    );
    private static final Map<String, Set<String>> ROLE_REQUIREMENTS = Map.of(
            //user service
            "user/profile", Set.of("USER", "ADMIN"),
            "user/profile", Set.of("USER", "ADMIN"),
            "user/profile", Set.of("USER", "ADMIN"),
            "user/profile", Set.of("USER", "ADMIN")
    );



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        if (isPublicRoute(path))
            return chain.filter(exchange);

        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader==null || !authHeader.startsWith("Bearer "))
            return unauthorized(exchange, "Missing JWT token");

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token))
            return unauthorized(exchange, "Invalid JWT token");

        if (!hasRequiredRole(token, path))
            return forbidden(exchange, "Insufficient permissions");

        return chain.filter(exchange.mutate().request(request).build());
    }

    private boolean isPublicRoute(String path){
        return PUBLIC_ROUTES.stream().anyMatch(path::startsWith);
    }

    private Mono<Void> unauthorized (ServerWebExchange exchange, String message){
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

    private boolean hasRequiredRole(String token, String path){
        String requirePath = ROLE_REQUIREMENTS.keySet().stream()
                .filter(path::startsWith)
                .findFirst()
                .orElse(null);
        if (requirePath == null)
            return true;

        Set<String> requiredRoles = ROLE_REQUIREMENTS.get(requirePath);
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
