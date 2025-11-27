package com.microshop.product.dto;

import com.microshop.product.model.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para devolver información de productos al cliente.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String category;
    private LocalDateTime createdAt;

    /**
     * Constructor por defecto.
     */
    public ProductResponseDTO() {
    }

    /**
     * Constructor con parámetros.
     * 
     * @param id ID del producto
     * @param name Nombre del producto
     * @param description Descripción del producto
     * @param price Precio del producto
     * @param stock Stock disponible
     * @param category Categoría del producto
     * @param createdAt Fecha de creación
     */
    public ProductResponseDTO(Long id, String name, String description, BigDecimal price, 
                             Integer stock, String category, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.createdAt = createdAt;
    }

    /**
     * Constructor estático para crear un DTO desde una entidad Product.
     * 
     * @param product Entidad Product
     * @return ProductResponseDTO con los datos del producto
     */
    public static ProductResponseDTO fromEntity(Product product) {
        return new ProductResponseDTO(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStock(),
            product.getCategory(),
            product.getCreatedAt()
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

