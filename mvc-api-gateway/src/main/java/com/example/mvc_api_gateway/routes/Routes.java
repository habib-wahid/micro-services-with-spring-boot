package com.example.mvc_api_gateway.routes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Objects;

@Configuration
public class Routes {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Bean
    public RouterFunction<ServerResponse> productServiceRoutes() {
        String productServiceUrl = resolveServiceUrl("product-service");
        return GatewayRouterFunctions.route("product-service")
                .route(RequestPredicates.path("/api/product"),
                        HandlerFunctions.http(productServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoutes() {
        String orderServiceUrl = resolveServiceUrl("order-service");
        return GatewayRouterFunctions.route("order-service")
                .route(RequestPredicates.path("/api/order"), HandlerFunctions.http(orderServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRoutes() {
        String inventoryServiceUrl = resolveServiceUrl("inventory-service");
        return GatewayRouterFunctions.route("inventory-service")
                .route(RequestPredicates.path("/api/inventory"), HandlerFunctions.http(inventoryServiceUrl))
                .build();
    }


    public String resolveServiceUrl(String serviceName) {
        return discoveryClient.getInstances(serviceName)
                .stream()
                .findFirst()
                .map(ServiceInstance::getUri)
                .map(Objects::toString)
                .orElse(null);
    }
}
