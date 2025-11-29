package com.blockflow.repository;

import com.blockflow.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Product entity operations.
 * Extends JpaRepository to provide standard CRUD operations.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds products where the name contains the given string (case-insensitive).
     *
     * @param name The name substring to search for.
     * @return List of matching products.
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /**
     * Finds products with stock quantity greater than the specified value.
     *
     * @param stock The minimum stock quantity.
     * @return List of products with sufficient stock.
     */
    List<Product> findByStockQuantityGreaterThan(Integer stock);

    /**
     * Finds products by their exact dimensions.
     *
     * @param dimensions The dimensions string.
     * @return List of matching products.
     */
    List<Product> findByDimensions(String dimensions);

    /**
     * Retrieves all products ordered by name in ascending order.
     *
     * @return List of all products sorted by name.
     */
    List<Product> findAllByOrderByNameAsc();
}
