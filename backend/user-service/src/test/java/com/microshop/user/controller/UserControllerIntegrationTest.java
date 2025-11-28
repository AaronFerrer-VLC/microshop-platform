package com.microshop.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microshop.user.dto.UserRequestDTO;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración para UserController.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.autoconfigure.exclude=org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration,org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration,org.springframework.cloud.netflix.eureka.EurekaServiceRegistryAutoConfiguration"
    }
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "eureka.client.enabled=false",
    "eureka.client.register-with-eureka=false",
    "eureka.client.fetch-registry=false",
    "spring.cloud.discovery.enabled=false",
    "spring.cloud.service-registry.auto-registration.enabled=false",
    "spring.cloud.config.enabled=false"
})
@Transactional
@DisplayName("UserController Integration Tests")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoderService passwordEncoderService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        
        testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPasswordHash(passwordEncoderService.encode("password123"));
        testUser.setRole(User.UserRole.CUSTOMER);
        testUser = userRepository.save(testUser);
    }

    @Test
    @DisplayName("Debería obtener todos los usuarios")
    @WithMockUser(roles = "ADMIN")
    void shouldGetAllUsers() throws Exception {
        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Test User"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }

    @Test
    @DisplayName("Debería obtener un usuario por ID")
    @WithMockUser(roles = "ADMIN")
    void shouldGetUserById() throws Exception {
        mockMvc.perform(get("/users/{id}", testUser.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("Debería retornar 404 cuando el usuario no existe")
    @WithMockUser(roles = "ADMIN")
    void shouldReturn404WhenUserNotFound() throws Exception {
        mockMvc.perform(get("/users/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Debería crear un nuevo usuario")
    @WithMockUser(roles = "ADMIN")
    void shouldCreateUser() throws Exception {
        UserRequestDTO newUser = new UserRequestDTO();
        newUser.setName("New User");
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("password123");
        newUser.setRole(User.UserRole.CUSTOMER);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New User"))
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.passwordHash").doesNotExist()); // No debe exponer el hash
    }

    @Test
    @DisplayName("Debería retornar 400 cuando los datos son inválidos")
    @WithMockUser(roles = "ADMIN")
    void shouldReturn400WhenInvalidData() throws Exception {
        UserRequestDTO invalidUser = new UserRequestDTO();
        invalidUser.setName(""); // Nombre vacío
        invalidUser.setEmail("invalid-email"); // Email inválido
        invalidUser.setPassword("123"); // Contraseña muy corta
        invalidUser.setRole(User.UserRole.CUSTOMER);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debería actualizar un usuario existente")
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateUser() throws Exception {
        UserRequestDTO updateDTO = new UserRequestDTO();
        updateDTO.setName("Updated User");
        updateDTO.setEmail("test@example.com");
        updateDTO.setPassword("newPassword123");
        updateDTO.setRole(User.UserRole.ADMIN);

        mockMvc.perform(put("/users/{id}", testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated User"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    @DisplayName("Debería eliminar un usuario")
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/{id}", testUser.getId()))
                .andExpect(status().isNoContent());

        // Verificar que el usuario fue eliminado
        assert userRepository.findById(testUser.getId()).isEmpty();
    }

    @Test
    @DisplayName("Debería retornar 404 al eliminar usuario inexistente")
    @WithMockUser(roles = "ADMIN")
    void shouldReturn404WhenDeletingNonExistentUser() throws Exception {
        mockMvc.perform(delete("/users/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}

