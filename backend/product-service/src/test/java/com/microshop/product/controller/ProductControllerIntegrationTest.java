package com.microshop.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microshop.product.dto.ProductRequestDTO;
import com.microshop.product.model.Product;
import com.microshop.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración para ProductController.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("ProductController Integration Tests")
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        
        testProduct = new Product();
        testProduct.setName("Test Laptop");
        testProduct.setDescription("Test Laptop Description");
        testProduct.setPrice(new BigDecimal("999.99"));
        testProduct.setStock(10);
        testProduct.setCategory("Electronics");
        testProduct = productRepository.save(testProduct);
    }

    @Test
    @DisplayName("Debería obtener todos los productos")
    void shouldGetAllProducts() throws Exception {
        mockMvc.perform(get("/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Test Laptop"))
                .andExpect(jsonPath("$[0].price").value(999.99));
    }

    @Test
    @DisplayName("Debería obtener un producto por ID")
    void shouldGetProductById() throws Exception {
        mockMvc.perform(get("/products/{id}", testProduct.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testProduct.getId()))
                .andExpect(jsonPath("$.name").value("Test Laptop"))
                .andExpect(jsonPath("$.price").value(999.99))
                .andExpect(jsonPath("$.category").value("Electronics"));
    }

    @Test
    @DisplayName("Debería retornar 404 cuando el producto no existe")
    void shouldReturn404WhenProductNotFound() throws Exception {
        mockMvc.perform(get("/products/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Debería buscar productos por categoría")
    void shouldSearchProductsByCategory() throws Exception {
        // Crear otro producto de la misma categoría
        Product product2 = new Product();
        product2.setName("Test Mouse");
        product2.setDescription("Test Mouse Description");
        product2.setPrice(new BigDecimal("29.99"));
        product2.setStock(50);
        product2.setCategory("Electronics");
        productRepository.save(product2);

        mockMvc.perform(get("/products/search")
                .param("category", "Electronics")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].category").value("Electronics"))
                .andExpect(jsonPath("$[1].category").value("Electronics"));
    }

    @Test
    @DisplayName("Debería crear un nuevo producto")
    void shouldCreateProduct() throws Exception {
        ProductRequestDTO newProduct = new ProductRequestDTO();
        newProduct.setName("New Product");
        newProduct.setDescription("New Product Description");
        newProduct.setPrice(new BigDecimal("199.99"));
        newProduct.setStock(25);
        newProduct.setCategory("Accessories");

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Product"))
                .andExpect(jsonPath("$.price").value(199.99))
                .andExpect(jsonPath("$.stock").value(25))
                .andExpect(jsonPath("$.category").value("Accessories"));
    }

    @Test
    @DisplayName("Debería retornar 400 cuando los datos son inválidos")
    void shouldReturn400WhenInvalidData() throws Exception {
        ProductRequestDTO invalidProduct = new ProductRequestDTO();
        invalidProduct.setName(""); // Nombre vacío
        invalidProduct.setPrice(new BigDecimal("-10.00")); // Precio negativo
        invalidProduct.setStock(-5); // Stock negativo
        invalidProduct.setCategory(""); // Categoría vacía

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidProduct)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debería actualizar un producto existente")
    void shouldUpdateProduct() throws Exception {
        ProductRequestDTO updateDTO = new ProductRequestDTO();
        updateDTO.setName("Updated Laptop");
        updateDTO.setDescription("Updated Description");
        updateDTO.setPrice(new BigDecimal("1299.99"));
        updateDTO.setStock(5);
        updateDTO.setCategory("Electronics");

        mockMvc.perform(put("/products/{id}", testProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Laptop"))
                .andExpect(jsonPath("$.price").value(1299.99))
                .andExpect(jsonPath("$.stock").value(5));
    }

    @Test
    @DisplayName("Debería eliminar un producto")
    void shouldDeleteProduct() throws Exception {
        mockMvc.perform(delete("/products/{id}", testProduct.getId()))
                .andExpect(status().isNoContent());

        // Verificar que el producto fue eliminado
        assert productRepository.findById(testProduct.getId()).isEmpty();
    }

    @Test
    @DisplayName("Debería retornar 404 al eliminar producto inexistente")
    void shouldReturn404WhenDeletingNonExistentProduct() throws Exception {
        mockMvc.perform(delete("/products/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}

