package com.microshop.user.controller;

import com.microshop.user.dto.LoginRequestDTO;
import com.microshop.user.dto.LoginResponseDTO;
import com.microshop.user.dto.UserResponseDTO;
import com.microshop.user.exception.InvalidCredentialsException;
import com.microshop.user.model.User;
import com.microshop.user.repository.UserRepository;
import com.microshop.user.service.JwtService;
import com.microshop.user.service.PasswordEncoderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para autenticación.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;
    private final JwtService jwtService;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param userRepository Repositorio de usuarios
     * @param passwordEncoderService Servicio de codificación de contraseñas
     * @param jwtService Servicio JWT
     */
    @Autowired
    public AuthController(UserRepository userRepository,
                          PasswordEncoderService passwordEncoderService,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
        this.jwtService = jwtService;
    }

    /**
     * Endpoint para autenticación de usuarios.
     * 
     * @param loginRequest DTO con email y contraseña
     * @return Respuesta con token JWT e información del usuario
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        // Buscar el usuario por email
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciales inválidas"));

        // Verificar la contraseña
        if (!passwordEncoderService.matches(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Credenciales inválidas");
        }

        // Generar token JWT
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        // Crear respuesta
        UserResponseDTO userResponse = UserResponseDTO.fromEntity(user);
        LoginResponseDTO loginResponse = new LoginResponseDTO(token, userResponse);

        return ResponseEntity.ok(loginResponse);
    }
}

