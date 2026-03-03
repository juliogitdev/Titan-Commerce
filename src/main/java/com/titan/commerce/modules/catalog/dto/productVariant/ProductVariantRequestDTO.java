package com.titan.commerce.modules.catalog.dto.productVariant;

import com.titan.commerce.modules.catalog.domain.ProductVariant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductVariantRequestDTO {

    @Schema(description = "ID do produto base ao qual esta variante pertence", example = "105")
    @NotNull
    private Long productId;

    @Schema(description = "Código único da variante (Stock Keeping Unit)", example = "NOTE-GMR-PRO-16GB-BLK")
    @NotBlank
    private String skuCode;

    @Schema(description = "Quantidade inicial em estoque", example = "50")
    private Integer stockQuantity;

    @Schema(description = "Preço de venda da variante", example = "3599.99")
    @NotNull
    private BigDecimal price;

    @Schema(description = "Atributos específicos da variante em formato JSON (Cor, Tamanho, etc.)", example = "{\"cor\": \"Preto\", \"memoria\": \"16GB\"}")
    private String attributes;

    @Schema(description = "Define se a variante está ativa para venda", example = "true")
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