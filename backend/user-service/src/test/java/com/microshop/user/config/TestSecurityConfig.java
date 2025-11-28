package com.microshop.user.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;

/**
 * Configuración de test para deshabilitar componentes problemáticos.
 * Esta clase se carga automáticamente cuando el perfil "test" está activo.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
@TestConfiguration
@Profile("test")
public class TestSecurityConfig {
    // Esta configuración puede usarse para mockear o deshabilitar componentes
    // si es necesario en el futuro
}

