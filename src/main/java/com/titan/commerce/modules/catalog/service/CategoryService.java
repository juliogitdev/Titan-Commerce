package com.titan.commerce.modules.catalog.service;

import com.titan.commerce.modules.catalog.domain.Category;
import com.titan.commerce.modules.catalog.dto.category.CategoryRequestDTO;
import com.titan.commerce.modules.catalog.dto.category.CategoryResponseDTO;
import com.titan.commerce.modules.catalog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    //retorna todas as categoria do banco
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAll(Boolean active){
        List<Category> categories;

        if(active == null || active){
            categories = repository.findByActiveTrue();
        }else{
            categories = repository.findByActiveFalse();
        }

        return categories.stream()
                .map(CategoryResponseDTO::new) // Chama o construtor do DTO para CADA item
                .collect(Collectors.toList());
    }

    //cria uma categoria
    @Transactional
    public CategoryResponseDTO create(CategoryRequestDTO categoryRequestDTO){
        //Verifica se não existe aquela slug já no banco
        if(repository.existsBySlug(categoryRequestDTO.getSlug())){
            throw new IllegalArgumentException("Já existe uma categoria com essa slug");
        }

        //transforma o DTO em entidade
        Category newCategory = categoryRequestDTO.toEntity();

        //verifica se possui parent
        if(categoryRequestDTO.getParentId() != null){
            if(!repository.existsById(categoryRequestDTO.getParentId())){
                throw new IllegalArgumentException("Referência de parent não existe");
            }

            newCategory.setParent(repository.getReferenceById(categoryRequestDTO.getParentId()));
        }

        //salva
        Category categorySaved = repository.save(newCategory);


        return new CategoryResponseDTO(categorySaved);
    }

    //Deletar uma categoria
    public boolean delete(Long id){
        if(repository.existsById(id)){
            Category category = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
            category.setActive(false);
            repository.save(category);
            return true;
        }

        return false;
    }

    //busca uma categoria por id
    @Transactional(readOnly = true)
    public CategoryResponseDTO findById(Long id){
        Category category = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));

        return new CategoryResponseDTO(category);
    }

    public List<CategoryResponseDTO> findByChildrens(Long id){
        if(!repository.existsById(id)){
            throw new IllegalArgumentException("Categoria pai não encontrada");
        }
        List<Category> subCategories = repository.findByParentId(id);
        return subCategories.stream()
                .map(CategoryResponseDTO::new)
                .collect(Collectors.toList());
    }


    @Transactional
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dtoRequest){
        Category categoryDB = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada no banco de dados"));

        if(!categoryDB.getSlug().equals(dtoRequest.getSlug())
                && repository.existsBySlug(dtoRequest.getSlug())){
            throw new IllegalArgumentException("Já existe uma categoria com está slug cadastrada");
        }

        categoryDB.setName(dtoRequest.getName());
        categoryDB.setSlug(dtoRequest.getSlug());
        if(dtoRequest.getActive() != null){
            categoryDB.setActive(dtoRequest.getActive());
        }

        if(dtoRequest.getParentId() != null){
            if(dtoRequest.getParentId().equals(categoryDB.getId())){
                throw new IllegalArgumentException("Uma categoria não pode ser pai dela mesmo");
            }

            if(!repository.existsById(dtoRequest.getParentId())){
                throw new IllegalArgumentException("Categoria pai não encontrada");
            }

            categoryDB.setParent(repository.getReferenceById(dtoRequest.getParentId()));
        }else{
            categoryDB.setParent(null);
        }

        Category categoryUpdated = repository.save(categoryDB);

        return new CategoryResponseDTO(categoryUpdated);


    }

    @Transactional
    public CategoryResponseDTO activate(Long id){
        Category category = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Falha ao encontrar categoria"));

        if(Boolean.TRUE.equals(category.getActive())){
            throw new IllegalArgumentException("Categoria já está ativada");
        }

        category.setActive(true);

        return new CategoryResponseDTO(category);

    }

}
