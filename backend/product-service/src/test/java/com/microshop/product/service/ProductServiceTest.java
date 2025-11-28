package com.microshop.product.service;

import com.microshop.product.dto.ProductRequestDTO;
import com.microshop.product.dto.ProductResponseDTO;
import com.microshop.product.exception.ProductNotFoundException;
import com.microshop.product.model.Product;
import com.microshop.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para ProductService.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private ProductRequestDTO productRequestDTO;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Laptop HP");
        testProduct.setDescription("Laptop HP 15.6 pulgadas");
        testProduct.setPrice(new BigDecimal("899.99"));
        testProduct.setStock(10);
        testProduct.setCategory("Electronics");
        testProduct.setCreatedAt(LocalDateTime.now());

        productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setName("Laptop HP");
        productRequestDTO.setDescription("Laptop HP 15.6 pulgadas");
        productRequestDTO.setPrice(new BigDecimal("899.99"));
        productRequestDTO.setStock(10);
        productRequestDTO.setCategory("Electronics");
    }

    @Test
    @DisplayName("Debería encontrar todos los productos")
    void shouldFindAllProducts() {
        // Given
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Mouse Logitech");
        product2.setDescription("Mouse inalámbrico");
        product2.setPrice(new BigDecimal("29.99"));
        product2.setStock(50);
        product2.setCategory("Accessories");
        product2.setCreatedAt(LocalDateTime.now());

        List<Product> products = Arrays.asList(testProduct, product2);
        when(productRepository.findAll()).thenReturn(products);

        // When
        List<ProductResponseDTO> result = productService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Laptop HP", result.get(0).getName());
        assertEquals("Mouse Logitech", result.get(1).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería encontrar un producto por ID")
    void shouldFindProductById() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // When
        ProductResponseDTO result = productService.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals("Laptop HP", result.getName());
        assertEquals(new BigDecimal("899.99"), result.getPrice());
        assertEquals("Electronics", result.getCategory());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando el producto no existe")
    void shouldThrowExceptionWhenProductNotFound() {
        // Given
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ProductNotFoundException.class, () -> productService.findById(999L));
        verify(productRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Debería encontrar productos por categoría")
    void shouldFindProductsByCategory() {
        // Given
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Teclado Mecánico");
        product2.setDescription("Teclado mecánico RGB");
        product2.setPrice(new BigDecimal("79.99"));
        product2.setStock(30);
        product2.setCategory("Electronics");
        product2.setCreatedAt(LocalDateTime.now());

        List<Product> products = Arrays.asList(testProduct, product2);
        when(productRepository.findByCategoryIgnoreCase("Electronics")).thenReturn(products);

        // When
        List<ProductResponseDTO> result = productService.findByCategory("Electronics");

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Electronics", result.get(0).getCategory());
        assertEquals("Electronics", result.get(1).getCategory());
        verify(productRepository, times(1)).findByCategoryIgnoreCase("Electronics");
    }

    @Test
    @DisplayName("Debería crear un nuevo producto")
    void shouldCreateProduct() {
        // Given
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When
        ProductResponseDTO result = productService.create(productRequestDTO);

        // Then
        assertNotNull(result);
        assertEquals("Laptop HP", result.getName());
        assertEquals(new BigDecimal("899.99"), result.getPrice());
        assertEquals(10, result.getStock());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Debería actualizar un producto existente")
    void shouldUpdateProduct() {
        // Given
        ProductRequestDTO updateDTO = new ProductRequestDTO();
        updateDTO.setName("Laptop HP Updated");
        updateDTO.setDescription("Laptop HP 15.6 pulgadas - Actualizado");
        updateDTO.setPrice(new BigDecimal("999.99"));
        updateDTO.setStock(5);
        updateDTO.setCategory("Electronics");

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Laptop HP Updated");
        updatedProduct.setDescription("Laptop HP 15.6 pulgadas - Actualizado");
        updatedProduct.setPrice(new BigDecimal("999.99"));
        updatedProduct.setStock(5);
        updatedProduct.setCategory("Electronics");
        updatedProduct.setCreatedAt(LocalDateTime.now());

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // When
        ProductResponseDTO result = productService.update(1L, updateDTO);

        // Then
        assertNotNull(result);
        assertEquals("Laptop HP Updated", result.getName());
        assertEquals(new BigDecimal("999.99"), result.getPrice());
        assertEquals(5, result.getStock());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Debería eliminar un producto")
    void shouldDeleteProduct() {
        // Given
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        // When
        productService.delete(1L);

        // Then
        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debería lanzar excepción al eliminar producto inexistente")
    void shouldThrowExceptionWhenDeletingNonExistentProduct() {
        // Given
        when(productRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThrows(ProductNotFoundException.class, () -> productService.delete(999L));
        verify(productRepository, times(1)).existsById(999L);
        verify(productRepository, never()).deleteById(anyLong());
    }
}

