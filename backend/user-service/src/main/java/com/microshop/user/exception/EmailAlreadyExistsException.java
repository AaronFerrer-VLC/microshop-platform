package com.microshop.user.exception;

/**
 * Excepción lanzada cuando se intenta crear o actualizar un usuario
 * con un email que ya existe en el sistema.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
public class EmailAlreadyExistsException extends RuntimeException {

    /**
     * Constructor con mensaje.
     * 
     * @param message Mensaje de error
     */
    public EmailAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa.
     * 
     * @param message Mensaje de error
     * @param cause Causa de la excepción
     */
    public EmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}

