package com.microshop.user.service;

import com.microshop.user.dto.UserRequestDTO;
import com.microshop.user.dto.UserResponseDTO;
import com.microshop.user.exception.EmailAlreadyExistsException;
import com.microshop.user.exception.UserNotFoundException;
import com.microshop.user.model.User;
import com.microshop.user.repository.UserRepository;
import com.microshop.user.util.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que contiene la lógica de negocio para la gestión de usuarios.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param userRepository Repositorio de usuarios
     * @param passwordEncoderService Servicio para codificar contraseñas
     */
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoderService passwordEncoderService) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
    }

    /**
     * Obtiene todos los usuarios del sistema.
     * 
     * @return Lista de DTOs de usuarios
     */
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un usuario por su ID.
     * 
     * @param id ID del usuario
     * @return DTO del usuario
     * @throws com.microshop.user.exception.UserNotFoundException si no existe
     */
    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        User user = findUserByIdOrThrow(id);
        return UserResponseDTO.fromEntity(user);
    }

    /**
     * Crea un nuevo usuario.
     * 
     * @param userRequestDTO DTO con los datos del usuario a crear
     * @return DTO del usuario creado
     * @throws com.microshop.user.exception.EmailAlreadyExistsException si el email ya existe
     */
    public UserResponseDTO create(UserRequestDTO userRequestDTO) {
        validateEmailNotExists(userRequestDTO.getEmail());
        
        String passwordHash = passwordEncoderService.encode(userRequestDTO.getPassword());
        User user = EntityMapper.toEntity(userRequestDTO, passwordHash);
        
        User savedUser = userRepository.save(user);
        return UserResponseDTO.fromEntity(savedUser);
    }

    /**
     * Actualiza un usuario existente.
     * 
     * @param id ID del usuario a actualizar
     * @param userRequestDTO DTO con los nuevos datos
     * @return DTO del usuario actualizado
     * @throws com.microshop.user.exception.UserNotFoundException si no existe
     * @throws com.microshop.user.exception.EmailAlreadyExistsException si el nuevo email ya está en uso
     */
    public UserResponseDTO update(Long id, UserRequestDTO userRequestDTO) {
        User user = findUserByIdOrThrow(id);
        
        validateEmailChange(user, userRequestDTO.getEmail());
        
        boolean shouldUpdatePassword = shouldUpdatePassword(userRequestDTO.getPassword());
        String newPasswordHash = shouldUpdatePassword 
            ? passwordEncoderService.encode(userRequestDTO.getPassword()) 
            : null;
        
        EntityMapper.updateEntity(user, userRequestDTO, shouldUpdatePassword, newPasswordHash);
        
        User updatedUser = userRepository.save(user);
        return UserResponseDTO.fromEntity(updatedUser);
    }

    /**
     * Elimina un usuario por su ID.
     * 
     * @param id ID del usuario a eliminar
     * @throws com.microshop.user.exception.UserNotFoundException si no existe
     */
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }

    // Métodos privados auxiliares

    /**
     * Busca un usuario por ID o lanza excepción si no existe.
     * 
     * @param id ID del usuario
     * @return Usuario encontrado
     * @throws UserNotFoundException si no existe
     */
    private User findUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + id));
    }

    /**
     * Valida que el email no exista en el sistema.
     * 
     * @param email Email a validar
     * @throws EmailAlreadyExistsException si el email ya existe
     */
    private void validateEmailNotExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Ya existe un usuario con el email: " + email);
        }
    }

    /**
     * Valida si se puede cambiar el email sin conflictos.
     * 
     * @param currentUser Usuario actual
     * @param newEmail Nuevo email
     * @throws EmailAlreadyExistsException si el nuevo email ya existe
     */
    private void validateEmailChange(User currentUser, String newEmail) {
        if (!currentUser.getEmail().equals(newEmail) && userRepository.existsByEmail(newEmail)) {
            throw new EmailAlreadyExistsException("Ya existe un usuario con el email: " + newEmail);
        }
    }

    /**
     * Determina si se debe actualizar la contraseña.
     * 
     * @param password Nueva contraseña (puede ser null o vacía)
     * @return true si se debe actualizar
     */
    private boolean shouldUpdatePassword(String password) {
        return password != null && !password.isEmpty();
    }
}

