package com.microshop.user.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;

/**
 * Configuración de test para excluir auto-configuraciones problemáticas.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
@TestConfiguration
@Profile("test")
public class TestConfig {
    // Esta clase puede usarse para configuraciones específicas de test si es necesario
}

