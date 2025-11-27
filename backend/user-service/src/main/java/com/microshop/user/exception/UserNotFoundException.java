package com.microshop.user.exception;

/**
 * Excepción lanzada cuando no se encuentra un usuario.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructor con mensaje.
     * 
     * @param message Mensaje de error
     */
    public UserNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa.
     * 
     * @param message Mensaje de error
     * @param cause Causa de la excepción
     */
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

