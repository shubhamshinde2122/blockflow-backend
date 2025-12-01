package com.blockflow.mapper;

import com.blockflow.dto.ProductRequest;
import com.blockflow.dto.ProductResponse;
import com.blockflow.model.Product;
import org.springframework.stereotype.Component;

// Force IDE refresh

/**
 * Mapper to convert between Product entity and DTOs.
 */
@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request) {
        return Product.builder()
                .name(request.getName())
                .dimensions(request.getDimensions())
                .pricePerUnit(request.getPricePerUnit())
                .stockQuantity(request.getStockQuantity())
                .description(request.getDescription())
                .weight(request.getWeight())
                .build();
    }

    public ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .dimensions(product.getDimensions())
                .pricePerUnit(product.getPricePerUnit())
                .stockQuantity(product.getStockQuantity())
                .description(product.getDescription())
                .weight(product.getWeight())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public void updateEntityFromRequest(Product product, ProductRequest request) {
        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDimensions() != null) {
            product.setDimensions(request.getDimensions());
        }
        if (request.getPricePerUnit() != null) {
            product.setPricePerUnit(request.getPricePerUnit());
        }
        if (request.getStockQuantity() != null) {
            product.setStockQuantity(request.getStockQuantity());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getWeight() != null) {
            product.setWeight(request.getWeight());
        }
    }
} // End of class
