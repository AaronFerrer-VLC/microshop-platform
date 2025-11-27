package com.microshop.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Clase principal del API Gateway.
 * El Gateway actúa como punto de entrada único para todas las peticiones
 * de los clientes, enrutando las solicitudes a los microservicios apropiados.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
@SpringBootApplication
@EnableEurekaClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}

