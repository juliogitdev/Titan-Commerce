package com.titan.commerce.modules.catalog.dto.productVariant;

import com.titan.commerce.modules.catalog.domain.ProductVariant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductVariantRequestDTO {
    @NotNull private Long productId;
    @NotBlank private String skuCode;
    private Integer stockQuantity;
    @NotNull private BigDecimal price;
    private String attributes;
    private Boolean active;

    public ProductVariantRequestDTO(Long productId, String skuCode, Integer stockQuantity, BigDecimal price, String attributes, Boolean active){
        this.productId = productId;
        this.skuCode = skuCode;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.attributes = attributes;
        this.active = active;
    }

    public ProductVariant toEntity(){
        ProductVariant newProductVariant = new ProductVariant();
        newProductVariant.setSkuCode(this.skuCode);
        newProductVariant.setStockQuantity(this.stockQuantity);
        newProductVariant.setPrice(this.price);
        newProductVariant.setAttributes(this.attributes);
        newProductVariant.setActive(this.active != null? this.active : true);
        return newProductVariant;
    }

}
