package com.titan.commerce.modules.catalog.dto.product;

import com.titan.commerce.modules.catalog.domain.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductRequestDTO {

    @Schema(description = "Título ou nome do produto", example = "Notebook Gamer Pro 15")
    @NotBlank
    private String title;

    @Schema(description = "Descrição detalhada do produto", example = "Notebook de alta performance com placa de vídeo RTX 4060, 16GB RAM e SSD de 1TB.")
    private String description;

    @Schema(description = "Marca ou fabricante do produto", example = "Dell Alienware")
    private String brand;

    @Schema(description = "ID da categoria à qual o produto pertence", example = "2")
    private Long categoryId;

    @Schema(description = "Define se o produto está ativo e visível na loja", example = "true")
    private Boolean active;

    public ProductRequestDTO(String title, String description, String brand, Long categoryId, Boolean active){
        this.title = title;
        this.description = description;
        this.brand = brand;
        this.categoryId = categoryId;
        this.active = active;
    }

    public Product toEntity(){
        Product newProduct = new Product();
        newProduct.setTitle(this.title);
        newProduct.setDescription(this.description);
        newProduct.setBrand(this.brand);
        newProduct.setActive(this.getActive() != null ? this.active : true);
        return newProduct;
    }
}