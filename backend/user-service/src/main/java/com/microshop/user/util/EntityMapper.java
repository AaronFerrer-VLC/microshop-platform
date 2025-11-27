package com.microshop.user.util;

import com.microshop.user.dto.UserRequestDTO;
import com.microshop.user.model.User;

/**
 * Utilidad para mapear entre entidades y DTOs.
 * Centraliza la lógica de conversión para evitar duplicación.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
public class EntityMapper {

    /**
     * Mapea un UserRequestDTO a una entidad User.
     * 
     * @param dto DTO con los datos del usuario
     * @param passwordHash Hash de la contraseña ya codificado
     * @return Entidad User
     */
    public static User toEntity(UserRequestDTO dto, String passwordHash) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordHash);
        user.setRole(dto.getRole());
        return user;
    }

    /**
     * Actualiza una entidad User con los datos de un DTO.
     * 
     * @param user Entidad a actualizar
     * @param dto DTO con los nuevos datos
     * @param updatePassword Si es true, actualiza la contraseña
     * @param newPasswordHash Nuevo hash de contraseña (solo se usa si updatePassword es true)
     */
    public static void updateEntity(User user, UserRequestDTO dto, boolean updatePassword, String newPasswordHash) {
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        
        if (updatePassword && newPasswordHash != null) {
            user.setPasswordHash(newPasswordHash);
        }
    }
}

