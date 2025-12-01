package com.blockflow.controller;

import com.blockflow.dto.ProductRequest;
import com.blockflow.dto.ProductResponse;
import com.blockflow.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.blockflow.service.ProductSearchService;
import com.blockflow.dto.SearchResponse;
import com.blockflow.model.Product;
import com.blockflow.repository.ProductRepository;
import org.springframework.data.domain.Page;
import com.blockflow.mapper.ProductMapper;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = { RequestMethod.GET, RequestMethod.POST,
        RequestMethod.PUT, RequestMethod.DELETE })
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @Autowired
    private ProductSearchService productSearchService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @org.springframework.security.access.prepost.PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> createProduct(@RequestBody ProductRequest productRequest) {
        log.info("REST request to create product");
        productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        log.info("REST request to get all products");
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // ✅ NEW: Search endpoint
    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int limit) {
        try {
            List<Product> products = productSearchService.searchByKeyword(q);
            int start = page * limit;
            int end = Math.min(start + limit, products.size());
            List<Product> paginatedProducts = products.subList(start, end);

            // Convert Product to ProductResponse if needed, but SearchResponse takes
            // ProductResponse?
            // Wait, SearchResponse in Step 2 took List<ProductResponse>.
            // The user's code in Step 4 creates SearchResponse with List<Product>
            // (paginatedProducts).
            // My SearchResponse DTO (Step 2) expects List<ProductResponse>.
            // I need to map Product -> ProductResponse here.

            // Let's check SearchResponse definition again.
            // I defined it as: private List<ProductResponse> products;
            // The user's Step 4 code passes `paginatedProducts` which is `List<Product>`.
            // This will cause a compilation error.
            // I MUST fix this mapping.

            // I need ProductMapper.
            // ProductController doesn't have ProductMapper injected.
            // ProductServiceImpl has it.
            // I should inject ProductMapper into ProductController.

            // OR I can change SearchResponse to take List<Product> as per user's Step 2
            // request?
            // User's Step 2 request:
            // public class SearchResponse { private List<Product> products; ... }
            // BUT I implemented it as `List<ProductResponse>` because I thought it was
            // better practice.
            // User's Step 2 request explicitly said `private List<Product> products;`.
            // I deviated from the user's request in Step 2.
            // I should have followed it or I should now adapt the controller.

            // No, that's a step backward.
            // I'll check if I can easily inject ProductMapper.
            // Yes, just add it to fields.

            return ResponseEntity.ok(new SearchResponse(
                    paginatedProducts.stream().map(productMapper::toResponse)
                            .collect(java.util.stream.Collectors.toList()),
                    page, limit, products.size(), (products.size() + limit - 1) / limit));

        } catch (Exception e) {
            return ResponseEntity.status(400).body("Search failed: " + e.getMessage());
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<ProductResponse>> getAvailableProducts() {
        log.info("REST request to get available products");
        return ResponseEntity.ok(productService.getAvailableProducts());
    }

    @DeleteMapping("/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("REST request to delete product with id: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ NEW: Filter endpoint
    @GetMapping("/filter")
    public ResponseEntity<?> filterProducts(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") Double minPrice,
            @RequestParam(defaultValue = "1000000") Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int limit) {
        try {
            Page<Product> products = productSearchService.filterByCategory(category, minPrice, maxPrice, page, limit);
            return ResponseEntity.ok(products.map(productMapper::toResponse));
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Filter failed: " + e.getMessage());
        }
    }

    // ✅ NEW: Sort endpoint
    @GetMapping("/sort")
    public ResponseEntity<?> sortProducts(
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int limit) {
        try {
            Page<Product> products = productSearchService.sortProducts(sortBy, page, limit);
            return ResponseEntity.ok(products.map(productMapper::toResponse));
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Sort failed: " + e.getMessage());
        }
    }

    // ✅ NEW: Advanced search (combination of all)
    @GetMapping("/search-advanced")
    public ResponseEntity<?> advancedSearch(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") Double minPrice,
            @RequestParam(defaultValue = "1000000") Double maxPrice,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int limit) {
        try {
            Page<Product> products = productSearchService.advancedSearch(q, category, minPrice, maxPrice, sortBy, page,
                    limit);
            return ResponseEntity.ok(products.map(productMapper::toResponse));
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Advanced search failed: " + e.getMessage());
        }
    }

    // ✅ NEW: Track product views
    @GetMapping("/{id}/view")
    public ResponseEntity<?> viewProduct(@PathVariable Long id) {
        try {
            com.blockflow.dto.ProductResponse product = productService.getProductById(id); // Service returns
                                                                                           // ProductResponse
            productSearchService.incrementViewCount(id);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Product not found");
        }
    }

    // ✅ NEW: Get all categories
    @GetMapping("/categories/all")
    public ResponseEntity<?> getAllCategories() {
        try {
            List<String> categories = productRepository.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to fetch categories");
        }
    }
}
