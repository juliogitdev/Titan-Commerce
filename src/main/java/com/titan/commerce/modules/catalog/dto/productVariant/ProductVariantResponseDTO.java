package com.titan.commerce.modules.catalog.dto.productVariant;

import com.titan.commerce.modules.catalog.domain.ProductVariant;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductVariantResponseDTO {
    private Long id;
    private Long productId;
    private String productTitle;
    private String skuCode;
    private Integer stockQuantity;
    private BigDecimal price;
    private String attributes;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductVariantResponseDTO(ProductVariant productVariant){
        this.id = productVariant.getId();
        this.productId = productVariant.getProduct().getId();
        this.productTitle = productVariant.getProduct().getTitle();
        this.skuCode = productVariant.getSkuCode();
        this.stockQuantity = productVariant.getStockQuantity();
        this.price = productVariant.getPrice();
        this.attributes = productVariant.getAttributes();
        this.active = productVariant.getActive();
        this.createdAt = productVariant.getCreatedAt();
        this.updatedAt = productVariant.getUpdatedAt();
    }
}
