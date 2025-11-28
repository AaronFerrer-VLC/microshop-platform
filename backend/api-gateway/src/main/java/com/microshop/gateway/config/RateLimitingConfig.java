package com.microshop.gateway.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Configuración de Rate Limiting para el API Gateway.
 * Protege contra ataques de fuerza bruta y abuso de API.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
@Configuration
public class RateLimitingConfig {

    // Almacenamiento en memoria de buckets por IP
    // En producción, usar Redis para distribución
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    /**
     * Configuración de rate limiting:
     * - 100 peticiones por minuto por IP para endpoints generales
     * - 10 peticiones por minuto por IP para endpoints de autenticación
     */
    private static final int GENERAL_REQUESTS_PER_MINUTE = 100;
    private static final int AUTH_REQUESTS_PER_MINUTE = 10;

    /**
     * Crea un bucket para rate limiting general.
     * 
     * @return Bucket configurado
     */
    private Bucket createGeneralBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.simple(
                    GENERAL_REQUESTS_PER_MINUTE,
                    Duration.ofMinutes(1)
                ))
                .build();
    }

    /**
     * Crea un bucket para rate limiting de autenticación (más restrictivo).
     * 
     * @return Bucket configurado
     */
    private Bucket createAuthBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.simple(
                    AUTH_REQUESTS_PER_MINUTE,
                    Duration.ofMinutes(1)
                ))
                .build();
    }

    /**
     * Obtiene o crea un bucket para una IP específica.
     * 
     * @param ip Dirección IP del cliente
     * @param isAuthEndpoint Si es un endpoint de autenticación
     * @return Bucket para la IP
     */
    private Bucket resolveBucket(String ip, boolean isAuthEndpoint) {
        String key = isAuthEndpoint ? "auth:" + ip : "general:" + ip;
        return cache.computeIfAbsent(key, k -> 
            isAuthEndpoint ? createAuthBucket() : createGeneralBucket()
        );
    }

    /**
     * Extrae la IP del cliente de la petición.
     * 
     * @param exchange ServerWebExchange
     * @return IP del cliente
     */
    private String getClientIP(ServerWebExchange exchange) {
        String xForwardedFor = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIP = exchange.getRequest().getHeaders().getFirst("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }
        
        if (exchange.getRequest().getRemoteAddress() != null 
                && exchange.getRequest().getRemoteAddress().getAddress() != null) {
            return exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        }
        
        return "unknown";
    }

    /**
     * Filtro de rate limiting que se aplica a todas las peticiones.
     * 
     * @return WebFilter configurado
     */
    @Bean
    public WebFilter rateLimitingFilter() {
        return (ServerWebExchange exchange, WebFilterChain chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            boolean isAuthEndpoint = path.contains("/auth/") || path.contains("/api/auth/");
            
            String clientIP = getClientIP(exchange);
            Bucket bucket = resolveBucket(clientIP, isAuthEndpoint);
            
            if (bucket.tryConsume(1)) {
                return chain.filter(exchange);
            } else {
                // Rate limit excedido
                exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.TOO_MANY_REQUESTS);
                exchange.getResponse().getHeaders().add("X-RateLimit-Limit", 
                    String.valueOf(isAuthEndpoint ? AUTH_REQUESTS_PER_MINUTE : GENERAL_REQUESTS_PER_MINUTE));
                exchange.getResponse().getHeaders().add("X-RateLimit-Retry-After", "60");
                return exchange.getResponse().setComplete();
            }
        };
    }
}

