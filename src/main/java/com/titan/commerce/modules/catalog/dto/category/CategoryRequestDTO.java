package com.titan.commerce.modules.catalog.dto.category;

import com.titan.commerce.modules.catalog.domain.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryRequestDTO{
    @NotBlank private String name;
    @NotBlank private String slug;
    private Long parentId;
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
