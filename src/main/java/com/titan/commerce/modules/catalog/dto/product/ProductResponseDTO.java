package com.titan.commerce.modules.catalog.dto.product;

import com.titan.commerce.modules.catalog.domain.Product;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String brand;
    private Long categoryId;
    private String categoryName;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductResponseDTO(Product product){
        this.id = product.getId();
        this.title = product.getTitle();
        this.description = product.getDescription();
        this.brand = product.getBrand();
        this.active = product.getActive();
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();

        if (product.getCategory() != null){
            this.categoryId = product.getCategory().getId();
            this.categoryName = product.getCategory().getName();
        } else {
            this.categoryId= null;
            this.categoryName = null;
        }
    }

}
