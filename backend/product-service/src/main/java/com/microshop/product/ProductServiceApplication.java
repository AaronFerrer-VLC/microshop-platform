package com.microshop.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Clase principal del servicio de productos.
 * Este microservicio gestiona el catálogo de productos, incluyendo
 * información de precios, stock y categorías.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
@SpringBootApplication
@EnableEurekaClient
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}

