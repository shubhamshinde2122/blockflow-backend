package com.blockflow.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.blockflow.model.Product;
import com.blockflow.repository.ProductRepository;
import java.util.List;
import java.math.BigDecimal;

@Service
public class ProductSearchService {

    private final ProductRepository productRepository;

    public ProductSearchService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ✅ Search by name or description
    public List<Product> searchByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll();
        }
        // Note: The repository method uses LIKE %keyword%, so we just pass the keyword
        // actually the repo query uses CONCAT('%', :keyword, '%'), so we pass the raw
        // keyword.
        // Wait, the user's code did: String searchTerm = "%" + keyword.toLowerCase() +
        // "%";
        // My repo query is: LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        // So I should just pass the keyword.
        return productRepository.searchByNameOrDescription(keyword);
    }

    // ✅ Filter by category and price range
    public Page<Product> filterByCategory(String category, Double minPrice, Double maxPrice, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        BigDecimal min = BigDecimal.valueOf(minPrice != null ? minPrice : 0.0);
        BigDecimal max = BigDecimal.valueOf(maxPrice != null ? maxPrice : Double.MAX_VALUE);

        if (category != null && !category.isEmpty()) {
            return productRepository.findByCategoryAndPriceBetween(category, min, max, pageable);
        } else {
            return productRepository.findByPriceBetween(min, max, pageable);
        }
    }

    // ✅ Sort by different criteria
    public Page<Product> sortProducts(String sortBy, int page, int limit) {
        Pageable pageable;

        switch (sortBy) {
            case "price_asc":
                pageable = PageRequest.of(page, limit, Sort.by("pricePerUnit").ascending());
                break;
            case "price_desc":
                pageable = PageRequest.of(page, limit, Sort.by("pricePerUnit").descending());
                break;
            case "newest":
                pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
                break;
            case "popular":
                pageable = PageRequest.of(page, limit, Sort.by("viewCount").descending());
                break;
            default:
                pageable = PageRequest.of(page, limit, Sort.by("id").descending());
        }

        return productRepository.findAll(pageable);
    }

    // ✅ Search + Filter + Sort combined
    public Page<Product> advancedSearch(String keyword, String category, Double minPrice, Double maxPrice,
            String sortBy, int page, int limit) {
        Pageable pageable = createPageable(sortBy, page, limit);
        BigDecimal min = BigDecimal.valueOf(minPrice != null ? minPrice : 0.0);
        BigDecimal max = BigDecimal.valueOf(maxPrice != null ? maxPrice : Double.MAX_VALUE);

        if (keyword != null && !keyword.isEmpty()) {
            String searchTerm = "%" + keyword.toLowerCase() + "%";
            if (category != null && !category.isEmpty()) {
                return productRepository.advancedSearch(searchTerm, category, min, max, pageable);
            } else {
                return productRepository.searchWithPriceFilter(searchTerm, min, max, pageable);
            }
        } else {
            if (category != null && !category.isEmpty()) {
                return productRepository.findByCategoryAndPriceBetween(category, min, max, pageable);
            } else {
                return productRepository.findByPriceBetween(min, max, pageable);
            }
        }
    }

    private Pageable createPageable(String sortBy, int page, int limit) {
        switch (sortBy) {
            case "price_asc":
                return PageRequest.of(page, limit, Sort.by("pricePerUnit").ascending());
            case "price_desc":
                return PageRequest.of(page, limit, Sort.by("pricePerUnit").descending());
            case "newest":
                return PageRequest.of(page, limit, Sort.by("createdAt").descending());
            case "popular":
                return PageRequest.of(page, limit, Sort.by("viewCount").descending());
            default:
                return PageRequest.of(page, limit, Sort.by("id").descending());
        }
    }

    // ✅ Increment view count when product is viewed
    public void incrementViewCount(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setViewCount(product.getViewCount() + 1);
        productRepository.save(product);
    }
}
