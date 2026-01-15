package com.titan.commerce.modules.catalog.controller;

import com.titan.commerce.modules.catalog.domain.Category;
import com.titan.commerce.modules.catalog.dto.category.CategoryRequestDTO;
import com.titan.commerce.modules.catalog.dto.category.CategoryResponseDTO;
import com.titan.commerce.modules.catalog.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> listar(@RequestParam(required = false) Boolean active){
        return ResponseEntity.ok(service.findAll(active));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> criar(@RequestBody @Valid CategoryRequestDTO categoryDTO){
        CategoryResponseDTO newCategoryResponseDto = service.create(categoryDTO);
        return ResponseEntity.status(201).body(newCategoryResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        if(service.delete(id)){
            return ResponseEntity.ok().body("Categoria removida com sucesso");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não foi encontrada");
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/{id}/subcategories")
    public ResponseEntity<List<CategoryResponseDTO>> findByParentId(@PathVariable Long id){
        return ResponseEntity.ok(service.findByChildrens(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> update(@PathVariable @Valid Long id, @RequestBody CategoryRequestDTO requestDTO){
        CategoryResponseDTO categoriaUpdated = service.update(id, requestDTO);
        return ResponseEntity.ok(categoriaUpdated);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id){
        service.activate(id);

        return ResponseEntity.noContent().build();

    }
}
