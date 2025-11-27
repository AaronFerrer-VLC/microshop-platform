package com.microshop.product.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI (Swagger) para la documentación de la API.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configuración del bean OpenAPI con información de la API.
     * 
     * @return Configuración de OpenAPI
     */
    @Bean
    public OpenAPI productServiceOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8082");
        localServer.setDescription("Servidor local de desarrollo");

        Server gatewayServer = new Server();
        gatewayServer.setUrl("http://localhost:8080");
        gatewayServer.setDescription("API Gateway");

        Contact contact = new Contact();
        contact.setEmail("support@microshop.com");
        contact.setName("Microshop Platform");
        contact.setUrl("https://microshop.com");

        License license = new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.html");

        Info info = new Info()
                .title("Product Service API")
                .version("1.0.0")
                .contact(contact)
                .description("API REST para la gestión de productos del catálogo de Microshop Platform. " +
                            "Este servicio permite crear, consultar, actualizar y eliminar productos, " +
                            "así como buscar productos por categoría.")
                .termsOfService("https://microshop.com/terms")
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer, gatewayServer));
    }
}

