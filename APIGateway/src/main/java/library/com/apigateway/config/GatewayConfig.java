package library.com.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){

        return builder.routes()
                // catalog service
                .route("catalog-service", r->r
                        .path("/api/book/**")
                        .filters(f->f.stripPrefix(1))
                        .uri("http://localhost:8081"))

                //expected book service
                .route("expected-book-service", r->r
                        .path("/api/expected-book/**")
                        .filters(f->f.stripPrefix(1))
                        .uri("http://localhost:8082"))
                .route("expected-book-service", r->r
                        .path("/api/report-availability/**")
                        .filters(f->f.stripPrefix(1))
                        .uri("http://localhost:8082"))
                .route("expected-book-service", r->r
                        .path("/api/report-availability-error/**")
                        .filters(f->f.stripPrefix(1))
                        .uri("http://localhost:8082"))

                //event service
                .route("event-service", r->r
                        .path("/api/announcement/**")
                        .filters(f->f.stripPrefix(1))
                        .uri("http://localhost:8083"))
                .route("event-service", r->r
                        .path("/api/events/**")
                        .filters(f->f.stripPrefix(1))
                        .uri("http://localhost:8083"))
                .route("event-service", r->r
                        .path("/api/news/**")
                        .filters(f->f.stripPrefix(1))
                        .uri("http://localhost:8083"))

                //order service
                .route("order-service", r->r
                        .path("/api/reservation/**")
                        .filters(f->f.stripPrefix(1))
                        .uri("http://localhost:8084"))
                .route("order-service", r->r
                        .path("/api/cancel-reservation/**")
                        .filters(f->f.stripPrefix(1))
                        .uri("http://localhost:8084"))

                //user-service
                .route("user-service", r->r
                        .path("/api/user/**")
                        .filters(f->f.stripPrefix(1))
                        .uri("http://localhost:8087"))

                //auth service
                .route("auth-service", r->r
                        .path("/api/auth/**")
                        .filters(f->f
                                .stripPrefix(1).
                                setRequestHeader("Allow", "PATCH, POST, GET, PUT, DELETE"))
                        .uri("http://localhost:8086"))
                .route("auth-service", r->r
                .path("/api/reset-password/**")
                .filters(f->f.stripPrefix(1))
                .uri("http://localhost:8086"))
                .build();

    }


}
