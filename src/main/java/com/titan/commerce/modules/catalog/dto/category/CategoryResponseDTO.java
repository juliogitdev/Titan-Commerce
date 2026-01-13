package com.titan.commerce.modules.catalog.dto.category;

import com.titan.commerce.modules.catalog.domain.Category;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private String slug;
    private Long parentId;
    private String parentName;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedA;

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
