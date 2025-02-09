package com.app.cloud.filter;

import com.app.cloud.config.GatewayConfigProperties;
import com.app.cloud.exception.ServiceNotFoundException;
import com.app.cloud.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(-1)
public class JwtAuthenticationFilter implements GlobalFilter {

    private final WebClient.Builder webClientBuilder;
    private final GatewayConfigProperties gatewayConfigProperties;

    @Autowired
    public JwtAuthenticationFilter(WebClient.Builder webClientBuilder, GatewayConfigProperties gatewayConfigProperties) {
        this.webClientBuilder = webClientBuilder;
        this.gatewayConfigProperties = gatewayConfigProperties;
    }

    private static final String HEADER_STRING = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        // Respect the permitAll paths defined in security configuration
        if (path.startsWith(Constant.AUTH_BASE_URI)) {
            return chain.filter(exchange);
        }

        String header = exchange.getRequest().getHeaders().getFirst(HEADER_STRING);
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        String token = header.replace(TOKEN_PREFIX, "");

        // Retrieve the URI for the auth_service route
        String authServiceUri = gatewayConfigProperties.getRoutes().stream()
                .filter(route -> Constant.AUTH_SERVICE.equals(route.getId()))
                .map(GatewayConfigProperties.Route::getUri)
                .findFirst()
                .orElseThrow(() -> new ServiceNotFoundException());

        return webClientBuilder.build()
                .post()
                .uri(authServiceUri + Constant.AUTH_BASE_URI + Constant.VALIDATE_TOKEN_URI )
                .header(HEADER_STRING, TOKEN_PREFIX + token)
                .retrieve()
                .bodyToMono(Boolean.class)
                .flatMap(isValid -> {
                    if (Boolean.TRUE.equals(isValid)) {
                        return chain.filter(exchange);
                    } else {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }
                })
                .onErrorResume(e -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }
}
