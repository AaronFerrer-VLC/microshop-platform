package com.microshop.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del servicio de usuarios.
 * Este microservicio gestiona la información y operaciones relacionadas
 * con los usuarios de la plataforma.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}

