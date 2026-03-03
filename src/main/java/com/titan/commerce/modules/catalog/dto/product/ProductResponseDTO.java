package com.titan.commerce.modules.catalog.dto.product;

import com.titan.commerce.modules.catalog.domain.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductResponseDTO {

    @Schema(description = "ID único do produto", example = "105")
    private Long id;

    @Schema(description = "Título ou nome do produto", example = "Notebook Gamer Pro 15")
    private String title;

    @Schema(description = "Descrição detalhada do produto", example = "Notebook de alta performance com placa de vídeo RTX 4060, 16GB RAM e SSD de 1TB.")
    private String description;

    @Schema(description = "Marca ou fabricante do produto", example = "Dell Alienware")
    private String brand;

    @Schema(description = "ID da categoria à qual o produto pertence", example = "2")
    private Long categoryId;

    @Schema(description = "Nome da categoria associada", example = "Computadores")
    private String categoryName;

    @Schema(description = "Indica se o produto está ativo", example = "true")
    private Boolean active;

    @Schema(description = "Data em que o produto foi cadastrado")
    private LocalDateTime createdAt;

    @Schema(description = "Data da última modificação do produto")
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