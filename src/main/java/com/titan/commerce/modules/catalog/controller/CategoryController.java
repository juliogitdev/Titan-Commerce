package com.titan.commerce.modules.catalog.controller;

import com.titan.commerce.modules.catalog.domain.Category;
import com.titan.commerce.modules.catalog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    public ResponseEntity<List<Category>> listar(){
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<Category> criar(@RequestBody Category category){
        Category newCategory = service.create(category);
        return ResponseEntity.status(201).body(newCategory);
    }

}
