package com.microshop.product.exception;

/**
 * Excepción lanzada cuando no se encuentra un producto.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
public class ProductNotFoundException extends RuntimeException {

    /**
     * Constructor con mensaje.
     * 
     * @param message Mensaje de error
     */
    public ProductNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa.
     * 
     * @param message Mensaje de error
     * @param cause Causa de la excepción
     */
    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

