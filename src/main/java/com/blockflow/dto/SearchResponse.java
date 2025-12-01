package com.blockflow.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchResponse {
    private List<ProductResponse> products;
    private int currentPage;
    private int itemsPerPage;
    private long totalItems;
    private int totalPages;

    public SearchResponse(List<ProductResponse> products, int currentPage, int itemsPerPage, long totalItems,
            int totalPages) {
        this.products = products;
        this.currentPage = currentPage;
        this.itemsPerPage = itemsPerPage;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
    }
}
