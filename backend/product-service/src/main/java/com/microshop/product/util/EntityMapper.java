package com.microshop.product.util;

import com.microshop.product.dto.ProductRequestDTO;
import com.microshop.product.model.Product;

/**
 * Utilidad para mapear entre entidades y DTOs.
 * Centraliza la lógica de conversión para evitar duplicación.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
public class EntityMapper {

    /**
     * Mapea un ProductRequestDTO a una entidad Product.
     * 
     * @param dto DTO con los datos del producto
     * @return Entidad Product
     */
    public static Product toEntity(ProductRequestDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setCategory(dto.getCategory());
        return product;
    }

    /**
     * Actualiza una entidad Product con los datos de un DTO.
     * 
     * @param product Entidad a actualizar
     * @param dto DTO con los nuevos datos
     */
    public static void updateEntity(Product product, ProductRequestDTO dto) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setCategory(dto.getCategory());
    }
}

