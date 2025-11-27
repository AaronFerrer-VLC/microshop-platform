package com.microshop.user.dto;

/**
 * DTO para devolver la respuesta del login con el token JWT.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
public class LoginResponseDTO {

    private String token;
    private String type;
    private UserResponseDTO user;

    /**
     * Constructor por defecto.
     */
    public LoginResponseDTO() {
        this.type = "Bearer";
    }

    /**
     * Constructor con parámetros.
     * 
     * @param token Token JWT
     * @param user Información del usuario
     */
    public LoginResponseDTO(String token, UserResponseDTO user) {
        this.token = token;
        this.type = "Bearer";
        this.user = user;
    }

    // Getters y Setters

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }
}

