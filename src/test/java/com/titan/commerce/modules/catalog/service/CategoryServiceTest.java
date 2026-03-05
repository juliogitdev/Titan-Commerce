package com.titan.commerce.modules.catalog.service;


import com.titan.commerce.modules.catalog.domain.Category;
import com.titan.commerce.modules.catalog.dto.category.CategoryResponseDTO;
import com.titan.commerce.modules.catalog.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    CategoryRepository repository;

    @InjectMocks
    CategoryService service;

    private Category buildCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        category.setSlug("electronics");
        category.setActive(true);
        category.setParent(null);
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        return category;
    }

    @Test
    void shouldReturnActiveCategoriesWhenActiveIsNullOrTrue(){

        Category category = buildCategory();

        when(repository.findByActiveTrue()).thenReturn(List.of(category));

        List<CategoryResponseDTO> result = service.findAll(true);

        assertFalse(result.isEmpty());
        verify(repository).findByActiveTrue();
        verify(repository, never()).findByActiveFalse();

    }

    @Test
    void shouldReturnActiveCategoriesWhenActiveIsFalse(){

        Category category = buildCategory();
        category.setActive(Boolean.FALSE);
        when(repository.findByActiveFalse()).thenReturn(List.of(category));

        List<CategoryResponseDTO> result = service.findAll(Boolean.FALSE);

        assertFalse(result.isEmpty());
        verify(repository).findByActiveFalse();
        verify(repository, never()).findByActiveTrue();

    }

    @Test
    void shouldReturnEmptyListWhenNoActiveCategories(){

        when(repository.findByActiveTrue()).thenReturn(Collections.emptyList());

        List<CategoryResponseDTO> result = service.findAll(true);

        assertTrue(result.isEmpty());
        verify(repository).findByActiveTrue();
    }

    @Test
    void shouldReturnEmptyListWhenNoInactiveCategories(){
        when(repository.findByActiveFalse()).thenReturn(Collections.emptyList());

        List<CategoryResponseDTO> result = service.findAll(false);

        assertTrue(result.isEmpty());
        verify(repository).findByActiveFalse();
    }

}
