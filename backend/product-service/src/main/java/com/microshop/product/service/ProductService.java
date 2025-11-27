package com.microshop.product.service;

import com.microshop.product.dto.ProductRequestDTO;
import com.microshop.product.dto.ProductResponseDTO;
import com.microshop.product.exception.ProductNotFoundException;
import com.microshop.product.model.Product;
import com.microshop.product.repository.ProductRepository;
import com.microshop.product.util.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que contiene la lógica de negocio para la gestión de productos.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param productRepository Repositorio de productos
     */
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Obtiene todos los productos del catálogo.
     * 
     * @return Lista de DTOs de productos
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un producto por su ID.
     * 
     * @param id ID del producto
     * @return DTO del producto
     * @throws com.microshop.product.exception.ProductNotFoundException si no existe
     */
    @Transactional(readOnly = true)
    public ProductResponseDTO findById(Long id) {
        Product product = findProductByIdOrThrow(id);
        return ProductResponseDTO.fromEntity(product);
    }

    /**
     * Busca productos por categoría.
     * 
     * @param category Categoría a buscar
     * @return Lista de DTOs de productos de la categoría
     */
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findByCategory(String category) {
        return productRepository.findByCategoryIgnoreCase(category)
                .stream()
                .map(ProductResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Crea un nuevo producto.
     * 
     * @param productRequestDTO DTO con los datos del producto a crear
     * @return DTO del producto creado
     */
    public ProductResponseDTO create(ProductRequestDTO productRequestDTO) {
        Product product = EntityMapper.toEntity(productRequestDTO);
        Product savedProduct = productRepository.save(product);
        return ProductResponseDTO.fromEntity(savedProduct);
    }

    /**
     * Actualiza un producto existente.
     * 
     * @param id ID del producto a actualizar
     * @param productRequestDTO DTO con los nuevos datos
     * @return DTO del producto actualizado
     * @throws com.microshop.product.exception.ProductNotFoundException si no existe
     */
    public ProductResponseDTO update(Long id, ProductRequestDTO productRequestDTO) {
        Product product = findProductByIdOrThrow(id);
        EntityMapper.updateEntity(product, productRequestDTO);
        Product updatedProduct = productRepository.save(product);
        return ProductResponseDTO.fromEntity(updatedProduct);
    }

    /**
     * Elimina un producto por su ID.
     * 
     * @param id ID del producto a eliminar
     * @throws com.microshop.product.exception.ProductNotFoundException si no existe
     */
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Producto no encontrado con ID: " + id);
        }
        productRepository.deleteById(id);
    }

    // Métodos privados auxiliares

    /**
     * Busca un producto por ID o lanza excepción si no existe.
     * 
     * @param id ID del producto
     * @return Producto encontrado
     * @throws ProductNotFoundException si no existe
     */
    private Product findProductByIdOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con ID: " + id));
    }
}

