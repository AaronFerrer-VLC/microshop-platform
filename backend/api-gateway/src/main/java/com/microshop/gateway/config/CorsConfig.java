package com.microshop.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuración de CORS para el API Gateway.
 * Permite peticiones desde el frontend React.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
@Configuration
public class CorsConfig {

    /**
     * Configura el filtro CORS para Spring Cloud Gateway.
     * 
     * @return CorsWebFilter configurado
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        
        // Orígenes permitidos
        // En desarrollo: localhost con diferentes puertos
        // En producción: cambiar por el dominio real
        corsConfig.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173",  // Vite default port
            "http://localhost:3000",  // React default port
            "http://localhost:5174",  // Vite alternate port
            "http://127.0.0.1:5173",
            "http://127.0.0.1:3000"
        ));
        
        // Métodos HTTP permitidos
        corsConfig.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));
        
        // Headers permitidos
        corsConfig.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        
        // Headers expuestos al cliente
        corsConfig.setExposedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"
        ));
        
        // Permitir credenciales (cookies, auth headers)
        corsConfig.setAllowCredentials(true);
        
        // Tiempo de caché para preflight requests (1 hora)
        corsConfig.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        
        return new CorsWebFilter(source);
    }
}

