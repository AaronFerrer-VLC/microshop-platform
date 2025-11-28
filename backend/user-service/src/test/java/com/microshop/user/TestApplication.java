package com.microshop.user;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;

/**
 * Clase de aplicación de test que excluye auto-configuraciones problemáticas.
 * Se usa cuando el perfil "test" está activo.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
@TestConfiguration
@Profile("test")
public class TestApplication {
    // Esta clase puede usarse para configuraciones específicas de test
    // si es necesario excluir más auto-configuraciones
}

