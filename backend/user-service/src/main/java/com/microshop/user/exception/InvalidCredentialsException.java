package com.microshop.user.exception;

/**
 * Excepción lanzada cuando las credenciales de autenticación son inválidas.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
public class InvalidCredentialsException extends RuntimeException {

    /**
     * Constructor con mensaje.
     * 
     * @param message Mensaje de error
     */
    public InvalidCredentialsException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa.
     * 
     * @param message Mensaje de error
     * @param cause Causa de la excepción
     */
    public InvalidCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}

