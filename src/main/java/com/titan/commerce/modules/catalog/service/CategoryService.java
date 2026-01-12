package com.titan.commerce.modules.catalog.service;

import com.titan.commerce.modules.catalog.domain.Category;
import com.titan.commerce.modules.catalog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    //retorna todas as categoria do banco
    public List<Category> findAll(){
        return repository.findAll();
    }

    //cria uma categoria
    public Category create(Category category){
        if(repository.existsBySlug(category.getSlug())){
            throw new IllegalArgumentException("Já existe uma categoria com essa slug");
        }
        return repository.save(category);
    }
}
