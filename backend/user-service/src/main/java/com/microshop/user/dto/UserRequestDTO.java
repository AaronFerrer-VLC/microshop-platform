package com.microshop.user.dto;

import com.microshop.user.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para recibir datos de creación y actualización de usuarios.
 * Este DTO contiene la contraseña en texto plano que será hasheada
 * en el servicio antes de persistir.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
public class UserRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String password;

    @NotNull(message = "El rol es obligatorio")
    private User.UserRole role;

    /**
     * Constructor por defecto.
     */
    public UserRequestDTO() {
    }

    /**
     * Constructor con parámetros.
     * 
     * @param name Nombre del usuario
     * @param email Email del usuario
     * @param password Contraseña en texto plano
     * @param role Rol del usuario
     */
    public UserRequestDTO(String name, String email, String password, User.UserRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters y Setters

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User.UserRole getRole() {
        return role;
    }

    public void setRole(User.UserRole role) {
        this.role = role;
    }
}

