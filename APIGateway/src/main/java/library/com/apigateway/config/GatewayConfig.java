package library.com.apigateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final ServiceUrlsConfig config;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){

        return builder.routes()

                // catalog service
                .route("catalog-service", r->r
                        .path("/api/book/**")
                        .and()
                        .method(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.DELETE, HttpMethod.OPTIONS)
                        .filters(f->f.stripPrefix(1))
                        .uri(config.getCatalog()))

                //expected book service
                .route("expected-book-service", r->r
                        .path("/api/expected-book/**")
                        .filters(f->f.stripPrefix(1))
                        .uri(config.getExpectedBook()))
                .route("expected-book-service", r->r
                        .path("/api/report-availability/**")
                        .filters(f->f.stripPrefix(1))
                        .uri(config.getExpectedBook()))
                .route("expected-book-service", r->r
                        .path("/api/report-availability-error/**")
                        .filters(f->f.stripPrefix(1))
                        .uri(config.getExpectedBook()))

                //event service
                .route("event-service", r->r
                        .path("/api/announcement/**")
                        .filters(f->f.stripPrefix(1))
                        .uri(config.getEvent()))
                .route("event-service", r->r
                        .path("/api/events/**")
                        .filters(f->f.stripPrefix(1))
                        .uri(config.getEvent()))
                .route("event-service", r->r
                        .path("/api/news/**")
                        .filters(f->f.stripPrefix(1))
                        .uri(config.getEvent()))

                //order service
                .route("order-service", r->r
                        .path("/api/reservation/**")
                        .filters(f->f.stripPrefix(1))
                        .uri(config.getReservation()))
                .route("order-service", r->r
                        .path("/api/cancel-reservation/**")
                        .filters(f->f.stripPrefix(1))
                        .uri(config.getReservation()))

                //user-service
                .route("user-service", r->r
                        .path("/api/user/**")
                        .filters(f->f.stripPrefix(1))
                        .uri(config.getUser()))

                //auth service
                .route("auth-service", r->r
                        .path("/api/auth/**")
                        .filters(f->f
                                .stripPrefix(1).
                                setRequestHeader("Allow", "PATCH, POST, GET, PUT, DELETE"))
                        .uri(config.getAuth()))
                .route("auth-service", r->r
                .path("/api/reset-password/**")
                .filters(f->f.stripPrefix(1))
                .uri(config.getAuth()))
                .route("minio-images", r -> r
                        .path("/api/images/**")
                        .filters(f -> f.rewritePath(
                                "/api/images/(?<segment>.*)",
                                "/images/${segment}"
                        ))
                        .uri(config.getMinio()))
                .route("frontend-route", r ->
                        r.path("/**")
                                .uri(config.getFrontend()))
                .build();

    }


}
