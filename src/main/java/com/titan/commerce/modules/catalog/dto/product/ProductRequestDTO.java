package com.titan.commerce.modules.catalog.dto.product;

import com.titan.commerce.modules.catalog.domain.Product;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductRequestDTO {
    @NotBlank private String title;
    private String description;
    private String brand;
    private Long categoryId;
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
