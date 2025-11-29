package com.blockflow.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;

import com.blockflow.dto.ProductRequest;
import com.blockflow.dto.ProductResponse;
import com.blockflow.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST,
        RequestMethod.DELETE, RequestMethod.PUT })

/**
 * REST Controller for Product Management.
 * Provides endpoints for CRUD operations and searching.
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    /**
     * Get all products.
     *
     * @return List of all products.
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        log.info("REST request to get all products");
        return ResponseEntity.ok(productService.getAllProducts());
    }

    /**
     * Get a single product by ID.
     *
     * @param id The ID of the product.
     * @return The product if found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        log.info("REST request to get product : {}", id);
        return ResponseEntity.ok(productService.getProductById(id));
    }

    /**
     * Create a new product.
     *
     * @param productRequest The product data.
     * @return The created product.
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        log.info("REST request to create product : {}", productRequest.getName());
        ProductResponse createdProduct = productService.createProduct(productRequest);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    /**
     * Update an existing product.
     *
     * @param id             The ID of the product to update.
     * @param productRequest The updated product data.
     * @return The updated product.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
            @Valid @RequestBody ProductRequest productRequest) {
        log.info("REST request to update product : {}", id);
        return ResponseEntity.ok(productService.updateProduct(id, productRequest));
    }

    /**
     * Delete a product.
     *
     * @param id The ID of the product to delete.
     * @return No content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("REST request to delete product : {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search products by name.
     *
     * @param name The name substring to search for.
     * @return List of matching products.
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String name) {
        log.info("REST request to search products by name : {}", name);
        return ResponseEntity.ok(productService.searchByName(name));
    }

    /**
     * Get available products (stock > 0).
     *
     * @return List of available products.
     */
    @GetMapping("/available")
    public ResponseEntity<List<ProductResponse>> getAvailableProducts() {
        log.info("REST request to get available products");
        return ResponseEntity.ok(productService.getAvailableProducts());
    }
}
