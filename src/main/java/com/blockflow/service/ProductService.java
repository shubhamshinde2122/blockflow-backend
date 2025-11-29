package com.blockflow.service;

import com.blockflow.dto.ProductRequest;
import com.blockflow.dto.ProductResponse;

import java.util.List;

/**
 * Service interface for Product operations.
 */
public interface ProductService {
    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long id);

    ProductResponse createProduct(ProductRequest productRequest);

    ProductResponse updateProduct(Long id, ProductRequest productRequest);

    void deleteProduct(Long id);

    List<ProductResponse> searchByName(String name);

    List<ProductResponse> getAvailableProducts();
}
