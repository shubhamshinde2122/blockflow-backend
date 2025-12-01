package com.blockflow.repository;

import com.blockflow.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.math.BigDecimal;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

        // ✅ Search by name or description
        @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
        List<Product> searchByNameOrDescription(@Param("keyword") String keyword);

        // ✅ Filter by category and price
        @Query("SELECT p FROM Product p WHERE p.category = :category AND p.pricePerUnit BETWEEN :minPrice AND :maxPrice")
        Page<Product> findByCategoryAndPriceBetween(
                        @Param("category") String category,
                        @Param("minPrice") BigDecimal minPrice,
                        @Param("maxPrice") BigDecimal maxPrice,
                        Pageable pageable);

        // ✅ Filter by price range only
        @Query("SELECT p FROM Product p WHERE p.pricePerUnit BETWEEN :minPrice AND :maxPrice")
        Page<Product> findByPriceBetween(
                        @Param("minPrice") BigDecimal minPrice,
                        @Param("maxPrice") BigDecimal maxPrice,
                        Pageable pageable);

        // ✅ Advanced search with all filters
        @Query("SELECT p FROM Product p WHERE " +
                        "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) "
                        +
                        "AND p.category = :category " +
                        "AND p.pricePerUnit BETWEEN :minPrice AND :maxPrice")
        Page<Product> advancedSearch(
                        @Param("keyword") String keyword,
                        @Param("category") String category,
                        @Param("minPrice") BigDecimal minPrice,
                        @Param("maxPrice") BigDecimal maxPrice,
                        Pageable pageable);

        // ✅ Search with price filter (no category)
        @Query("SELECT p FROM Product p WHERE " +
                        "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) "
                        +
                        "AND p.pricePerUnit BETWEEN :minPrice AND :maxPrice")
        Page<Product> searchWithPriceFilter(
                        @Param("keyword") String keyword,
                        @Param("minPrice") BigDecimal minPrice,
                        @Param("maxPrice") BigDecimal maxPrice,
                        Pageable pageable);

        // ✅ Get all unique categories
        @Query("SELECT DISTINCT p.category FROM Product p WHERE p.category IS NOT NULL")
        List<String> getAllCategories();

        // Keep the existing method for finding by dimensions if needed, or other
        // existing methods?
        // The user said "REPLACE ENTIRE FILE". But I should check if other parts of the
        // app rely on other methods.
        // In Step 1 I saw `findAllByOrderByNameAsc`, `findByStockQuantityGreaterThan`,
        // `findByDimensions`.
        // `ProductServiceImpl` uses `findAllByOrderByNameAsc`,
        // `findByStockQuantityGreaterThan`, `findByDimensions` (maybe? no,
        // `searchByName` uses `findByNameContainingIgnoreCase`).
        // `ProductServiceImpl` uses:
        // - `findAllByOrderByNameAsc`
        // - `findById`
        // - `save`
        // - `deleteById`
        // - `existsById`
        // - `findByNameContainingIgnoreCase`
        // - `findByStockQuantityGreaterThan`

        // If I remove these, `ProductServiceImpl` will break.
        // The user said "REPLACE ENTIRE FILE", but they might not be aware of existing
        // dependencies.
        // I MUST preserve existing methods to avoid breaking the app.

        List<Product> findAllByOrderByNameAsc();

        List<Product> findByNameContainingIgnoreCase(String name);

        List<Product> findByStockQuantityGreaterThan(Integer stock);

        List<Product> findByDimensions(String dimensions);
}
