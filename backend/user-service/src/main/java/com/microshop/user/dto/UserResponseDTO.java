package com.microshop.user.dto;

import com.microshop.user.model.User;

import java.time.LocalDateTime;

/**
 * DTO para devolver información de usuarios al cliente.
 * Este DTO no incluye información sensible como el hash de contraseña.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private User.UserRole role;
    private LocalDateTime createdAt;

    /**
     * Constructor por defecto.
     */
    public UserResponseDTO() {
    }

    /**
     * Constructor con parámetros.
     * 
     * @param id ID del usuario
     * @param name Nombre del usuario
     * @param email Email del usuario
     * @param role Rol del usuario
     * @param createdAt Fecha de creación
     */
    public UserResponseDTO(Long id, String name, String email, User.UserRole role, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    /**
     * Constructor estático para crear un DTO desde una entidad User.
     * 
     * @param user Entidad User
     * @return UserResponseDTO con los datos del usuario
     */
    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole(),
            user.getCreatedAt()
        );
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User.UserRole getRole() {
        return role;
    }

    public void setRole(User.UserRole role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

