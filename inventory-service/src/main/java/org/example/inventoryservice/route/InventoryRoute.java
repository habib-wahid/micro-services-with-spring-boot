package org.example.inventoryservice.route;

import lombok.RequiredArgsConstructor;
import org.example.inventoryservice.service.InventoryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RequestPredicates.accept;

@Configuration
@RequiredArgsConstructor
public class InventoryRoute {

    private final InventoryService inventoryService;

    @Bean
    RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route()
                .GET("/api/inventory/hello-world", accept(MediaType.APPLICATION_JSON),
                        request -> ServerResponse.ok().body("Hello world")
                )
                .GET("/api/inventory/{id}", accept(MediaType.APPLICATION_JSON),
                        inventoryService::getInventoryById)
                .build();

    }
}
