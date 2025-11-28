package com.microshop.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microshop.user.dto.LoginRequestDTO;
import com.microshop.user.model.User;
import com.microshop.user.repository.UserRepository;
import com.microshop.user.service.PasswordEncoderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración para AuthController.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "eureka.client.enabled=false",
    "eureka.client.register-with-eureka=false",
    "eureka.client.fetch-registry=false",
    "spring.cloud.discovery.enabled=false",
    "spring.cloud.service-registry.auto-registration.enabled=false"
})
@Transactional
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoderService passwordEncoderService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private String plainPassword = "password123";

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        
        testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPasswordHash(passwordEncoderService.encode(plainPassword));
        testUser.setRole(User.UserRole.CUSTOMER);
        testUser = userRepository.save(testUser);
    }

    @Test
    @DisplayName("Debería hacer login exitosamente con credenciales válidas")
    void shouldLoginSuccessfully() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO("test@example.com", plainPassword);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.user").exists())
                .andExpect(jsonPath("$.user.email").value("test@example.com"))
                .andExpect(jsonPath("$.user.name").value("Test User"))
                .andExpect(jsonPath("$.user.passwordHash").doesNotExist()); // No debe exponer el hash
    }

    @Test
    @DisplayName("Debería retornar 401 con email incorrecto")
    void shouldReturn401WithWrongEmail() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO("wrong@example.com", plainPassword);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Debería retornar 401 con contraseña incorrecta")
    void shouldReturn401WithWrongPassword() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO("test@example.com", "wrongPassword");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Debería retornar 400 con datos inválidos")
    void shouldReturn400WithInvalidData() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO("", ""); // Email y password vacíos

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debería retornar 400 con email inválido")
    void shouldReturn400WithInvalidEmail() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO("invalid-email", plainPassword);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debería incluir el rol del usuario en el token")
    void shouldIncludeUserRoleInToken() throws Exception {
        // Crear usuario ADMIN
        User adminUser = new User();
        adminUser.setName("Admin User");
        adminUser.setEmail("admin@example.com");
        adminUser.setPasswordHash(passwordEncoderService.encode(plainPassword));
        adminUser.setRole(User.UserRole.ADMIN);
        userRepository.save(adminUser);

        LoginRequestDTO loginRequest = new LoginRequestDTO("admin@example.com", plainPassword);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.user.role").value("ADMIN"));
    }
}

