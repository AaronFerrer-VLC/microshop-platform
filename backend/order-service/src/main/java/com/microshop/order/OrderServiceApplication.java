package com.microshop.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Clase principal del servicio de pedidos.
 * Este microservicio gestiona los pedidos de los clientes, incluyendo
 * la creación, consulta y seguimiento de pedidos.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
@SpringBootApplication
@EnableEurekaClient
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}

