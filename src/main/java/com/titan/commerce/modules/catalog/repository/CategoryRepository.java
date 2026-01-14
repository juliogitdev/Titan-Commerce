package com.titan.commerce.modules.catalog.repository;

import com.titan.commerce.modules.catalog.domain.Category;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

    //busca pelo slug da categoria
    Optional<Category> findBySlug(String slug);

    //verifica se a slug existe
    boolean existsBySlug(String slug);

    List<Category> findByParentId(Long id);

    List<Category> findByActiveTrue();

    List<Category> findByActiveFalse();
}

