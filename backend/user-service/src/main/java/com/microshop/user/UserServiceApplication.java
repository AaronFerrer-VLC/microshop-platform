package com.microshop.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Clase principal del servicio de usuarios.
 * Este microservicio gestiona la información y operaciones relacionadas
 * con los usuarios de la plataforma.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
@SpringBootApplication
@EnableEurekaClient
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}

