package com.microshop.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio para codificar y validar contraseñas usando BCrypt.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
@Service
public class PasswordEncoderService {

    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor que inicializa el encoder BCrypt.
     */
    public PasswordEncoderService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Codifica una contraseña en texto plano usando BCrypt.
     * 
     * @param rawPassword Contraseña en texto plano
     * @return Hash BCrypt de la contraseña
     */
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Verifica si una contraseña en texto plano coincide con un hash BCrypt.
     * 
     * @param rawPassword Contraseña en texto plano
     * @param encodedPassword Hash BCrypt de la contraseña
     * @return true si coinciden, false en caso contrario
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}

