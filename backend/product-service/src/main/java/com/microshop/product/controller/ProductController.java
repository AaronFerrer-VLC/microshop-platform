package com.microshop.product.controller;

import com.microshop.product.dto.ProductRequestDTO;
import com.microshop.product.dto.ProductResponseDTO;
import com.microshop.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de productos.
 * Expone endpoints CRUD y de búsqueda para operaciones sobre productos.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
@RestController
@RequestMapping("/products")
@Tag(name = "Productos", description = "API para gestión de productos del catálogo")
public class ProductController {

    private final ProductService productService;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param productService Servicio de productos
     */
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Obtiene todos los productos del catálogo.
     * 
     * @return Lista de productos
     */
    @Operation(
        summary = "Listar todos los productos",
        description = "Obtiene una lista de todos los productos disponibles en el catálogo"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de productos obtenida exitosamente",
        content = @Content(mediaType = "application/json", 
                          schema = @Schema(implementation = ProductResponseDTO.class))
    )
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    /**
     * Obtiene un producto por su ID.
     * 
     * @param id ID del producto
     * @return Producto encontrado
     */
    @Operation(
        summary = "Obtener producto por ID",
        description = "Obtiene la información detallada de un producto específico por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Producto encontrado",
            content = @Content(mediaType = "application/json",
                             schema = @Schema(implementation = ProductResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(
            @Parameter(description = "ID del producto", required = true, example = "1")
            @PathVariable Long id) {
        ProductResponseDTO product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Busca productos por categoría.
     * 
     * @param category Categoría a buscar
     * @return Lista de productos de la categoría
     */
    @Operation(
        summary = "Buscar productos por categoría",
        description = "Obtiene una lista de productos filtrados por categoría. " +
                     "La búsqueda es case-insensitive."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de productos de la categoría",
            content = @Content(mediaType = "application/json",
                             schema = @Schema(implementation = ProductResponseDTO.class))
        )
    })
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> searchProductsByCategory(
            @Parameter(description = "Categoría del producto", required = true, example = "Electrónica")
            @RequestParam String category) {
        List<ProductResponseDTO> products = productService.findByCategory(category);
        return ResponseEntity.ok(products);
    }

    /**
     * Crea un nuevo producto.
     * 
     * @param productRequestDTO DTO con los datos del producto
     * @return Producto creado
     */
    @Operation(
        summary = "Crear nuevo producto",
        description = "Crea un nuevo producto en el catálogo con la información proporcionada"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Producto creado exitosamente",
            content = @Content(mediaType = "application/json",
                             schema = @Schema(implementation = ProductResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content
        )
    })
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos del producto a crear",
                required = true,
                content = @Content(schema = @Schema(implementation = ProductRequestDTO.class))
            )
            @Valid @RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO createdProduct = productService.create(productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    /**
     * Actualiza un producto existente.
     * 
     * @param id ID del producto a actualizar
     * @param productRequestDTO DTO con los nuevos datos
     * @return Producto actualizado
     */
    @Operation(
        summary = "Actualizar producto",
        description = "Actualiza la información de un producto existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Producto actualizado exitosamente",
            content = @Content(mediaType = "application/json",
                             schema = @Schema(implementation = ProductResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @Parameter(description = "ID del producto a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Nuevos datos del producto",
                required = true,
                content = @Content(schema = @Schema(implementation = ProductRequestDTO.class))
            )
            @Valid @RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO updatedProduct = productService.update(id, productRequestDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Elimina un producto por su ID.
     * 
     * @param id ID del producto a eliminar
     * @return Respuesta sin contenido
     */
    @Operation(
        summary = "Eliminar producto",
        description = "Elimina un producto del catálogo por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Producto eliminado exitosamente",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado",
            content = @Content
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID del producto a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

