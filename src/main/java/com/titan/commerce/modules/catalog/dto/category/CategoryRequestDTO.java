package com.titan.commerce.modules.catalog.dto.category;

import com.titan.commerce.modules.catalog.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryRequestDTO {

    @Schema(description = "Nome de exibição da categoria", example = "Eletrônicos")
    @NotBlank
    private String name;

    @Schema(description = "Identificador amigável para a URL", example = "eletronicos")
    @NotBlank
    private String slug;

    @Schema(description = "ID da categoria pai (se for uma subcategoria)", example = "1", nullable = true)
    private Long parentId;

    @Schema(description = "Status de ativação da categoria", example = "true")
    private Boolean active;

    public CategoryRequestDTO(String name, String slug, Long parentId, Boolean active){
        this.name = name;
        this.slug = slug;
        this.parentId = parentId;
        this.active = active;
    }

    public Category toEntity(){
        Category newCategory = new Category();
        newCategory.setName(this.getName());
        newCategory.setSlug(this.getSlug());
        newCategory.setActive(this.getActive() != null ? this.active : true);

        return newCategory;
    }
}