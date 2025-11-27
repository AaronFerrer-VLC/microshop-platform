package com.microshop.product.repository;

import com.microshop.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para operaciones de acceso a datos de productos.
 * Extiende JpaRepository proporcionando métodos CRUD básicos
 * y permite definir métodos personalizados.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Busca productos por categoría.
     * 
     * @param category Categoría a buscar
     * @return Lista de productos de la categoría especificada
     */
    List<Product> findByCategory(String category);

    /**
     * Busca productos por categoría ignorando mayúsculas/minúsculas.
     * 
     * @param category Categoría a buscar (case-insensitive)
     * @return Lista de productos de la categoría especificada
     */
    List<Product> findByCategoryIgnoreCase(String category);

    /**
     * Busca productos cuyo nombre contenga el texto especificado.
     * 
     * @param name Texto a buscar en el nombre
     * @return Lista de productos que coinciden
     */
    List<Product> findByNameContainingIgnoreCase(String name);
}

