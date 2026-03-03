package com.titan.commerce.modules.catalog.dto.category;

import com.titan.commerce.modules.catalog.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryResponseDTO {

    @Schema(description = "ID único da categoria", example = "10")
    private Long id;

    @Schema(description = "Nome de exibição da categoria", example = "Smartphones")
    private String name;

    @Schema(description = "Identificador amigável para a URL", example = "smartphones")
    private String slug;

    @Schema(description = "ID da categoria pai, se existir", example = "1", nullable = true)
    private Long parentId;

    @Schema(description = "Nome da categoria pai, se existir", example = "Eletrônicos", nullable = true)
    private String parentName;

    @Schema(description = "Indica se a categoria está ativa para o catálogo", example = "true")
    private Boolean active;

    @Schema(description = "Data de criação do registro")
    private LocalDateTime createdAt;

    @Schema(description = "Data da última atualização do registro")
    private LocalDateTime updatedA; // Nota: Mantive o nome original da sua variável

    public CategoryResponseDTO(Category category){
        this.id = category.getId();
        this.name = category.getName();
        this.slug = category.getSlug();
        this.active = category.getActive();
        this.createdAt = category.getCreatedAt();
        this.updatedA = category.getUpdatedAt();

        if (category.getParent() != null) {
            this.parentId = category.getParent().getId();
            this.parentName = category.getParent().getName();
        } else {
            this.parentId = null;
            this.parentName = null;
        }
    }
}