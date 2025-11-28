package com.microshop.user.service;

import com.microshop.user.dto.UserRequestDTO;
import com.microshop.user.dto.UserResponseDTO;
import com.microshop.user.exception.EmailAlreadyExistsException;
import com.microshop.user.exception.UserNotFoundException;
import com.microshop.user.model.User;
import com.microshop.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para UserService.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoderService passwordEncoderService;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserRequestDTO userRequestDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setPasswordHash("$2a$10$hashedPassword");
        testUser.setRole(User.UserRole.CUSTOMER);
        testUser.setCreatedAt(LocalDateTime.now());

        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("John Doe");
        userRequestDTO.setEmail("john.doe@example.com");
        userRequestDTO.setPassword("password123");
        userRequestDTO.setRole(User.UserRole.CUSTOMER);
    }

    @Test
    @DisplayName("Debería encontrar todos los usuarios")
    void shouldFindAllUsers() {
        // Given
        User user2 = new User();
        user2.setId(2L);
        user2.setName("Jane Doe");
        user2.setEmail("jane.doe@example.com");
        user2.setPasswordHash("$2a$10$hashedPassword2");
        user2.setRole(User.UserRole.ADMIN);
        user2.setCreatedAt(LocalDateTime.now());

        List<User> users = Arrays.asList(testUser, user2);
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<UserResponseDTO> result = userService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Doe", result.get(1).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería encontrar un usuario por ID")
    void shouldFindUserById() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        UserResponseDTO result = userService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando el usuario no existe")
    void shouldThrowExceptionWhenUserNotFound() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> userService.findById(999L));
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Debería crear un nuevo usuario")
    void shouldCreateUser() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoderService.encode(anyString())).thenReturn("$2a$10$hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        UserResponseDTO result = userService.create(userRequestDTO);

        // Then
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(userRepository, times(1)).existsByEmail("john.doe@example.com");
        verify(passwordEncoderService, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando el email ya existe al crear")
    void shouldThrowExceptionWhenEmailExistsOnCreate() {
        // Given
        when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(true);

        // When & Then
        assertThrows(EmailAlreadyExistsException.class, () -> userService.create(userRequestDTO));
        verify(userRepository, times(1)).existsByEmail("john.doe@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Debería actualizar un usuario existente")
    void shouldUpdateUser() {
        // Given
        UserRequestDTO updateDTO = new UserRequestDTO();
        updateDTO.setName("John Updated");
        updateDTO.setEmail("john.doe@example.com");
        updateDTO.setPassword("newPassword123");
        updateDTO.setRole(User.UserRole.ADMIN);

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("John Updated");
        updatedUser.setEmail("john.doe@example.com");
        updatedUser.setPasswordHash("$2a$10$newHashedPassword");
        updatedUser.setRole(User.UserRole.ADMIN);
        updatedUser.setCreatedAt(LocalDateTime.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(false);
        when(passwordEncoderService.encode("newPassword123")).thenReturn("$2a$10$newHashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When
        UserResponseDTO result = userService.update(1L, updateDTO);

        // Then
        assertNotNull(result);
        assertEquals("John Updated", result.getName());
        assertEquals(User.UserRole.ADMIN, result.getRole());
        verify(userRepository, times(1)).findById(1L);
        verify(passwordEncoderService, times(1)).encode("newPassword123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Debería actualizar usuario sin cambiar contraseña si no se proporciona")
    void shouldUpdateUserWithoutPassword() {
        // Given
        UserRequestDTO updateDTO = new UserRequestDTO();
        updateDTO.setName("John Updated");
        updateDTO.setEmail("john.doe@example.com");
        updateDTO.setPassword(null); // Sin contraseña
        updateDTO.setRole(User.UserRole.CUSTOMER);

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("John Updated");
        updatedUser.setEmail("john.doe@example.com");
        updatedUser.setPasswordHash("$2a$10$hashedPassword"); // Misma contraseña
        updatedUser.setRole(User.UserRole.CUSTOMER);
        updatedUser.setCreatedAt(LocalDateTime.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When
        UserResponseDTO result = userService.update(1L, updateDTO);

        // Then
        assertNotNull(result);
        assertEquals("John Updated", result.getName());
        verify(passwordEncoderService, never()).encode(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Debería eliminar un usuario")
    void shouldDeleteUser() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        // When
        userService.delete(1L);

        // Then
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debería lanzar excepción al eliminar usuario inexistente")
    void shouldThrowExceptionWhenDeletingNonExistentUser() {
        // Given
        when(userRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThrows(UserNotFoundException.class, () -> userService.delete(999L));
        verify(userRepository, times(1)).existsById(999L);
        verify(userRepository, never()).deleteById(anyLong());
    }
}

