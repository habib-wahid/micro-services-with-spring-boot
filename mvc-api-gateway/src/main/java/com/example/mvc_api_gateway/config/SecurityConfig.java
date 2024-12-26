package com.example.mvc_api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("Here comes first");
       return http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests.anyRequest().authenticated())
                .oauth2ResourceServer(oauth2ResourceServer ->oauth2ResourceServer.jwt(Customizer.withDefaults()))
                .build();
    }
}
