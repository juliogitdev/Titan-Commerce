package com.titan.commerce.modules.catalog.dto.productVariant;

import com.titan.commerce.modules.catalog.domain.ProductVariant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductVariantResponseDTO {

    @Schema(description = "ID único da variante", example = "340")
    private Long id;

    @Schema(description = "ID do produto base", example = "105")
    private Long productId;

    @Schema(description = "Título do produto base", example = "Notebook Gamer Pro 15")
    private String productTitle;

    @Schema(description = "Código único da variante (SKU)", example = "NOTE-GMR-PRO-16GB-BLK")
    private String skuCode;

    @Schema(description = "Quantidade atual em estoque", example = "45")
    private Integer stockQuantity;

    @Schema(description = "Preço de venda atual", example = "3599.99")
    private BigDecimal price;

    @Schema(description = "Atributos específicos da variante em formato JSON", example = "{\"cor\": \"Preto\", \"memoria\": \"16GB\"}")
    private String attributes;

    @Schema(description = "Status da variante", example = "true")
    private boolean active;

    @Schema(description = "Data de criação do registro")
    private LocalDateTime createdAt;

    @Schema(description = "Data da última atualização")
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