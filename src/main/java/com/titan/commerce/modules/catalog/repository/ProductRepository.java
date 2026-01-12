package com.titan.commerce.modules.catalog.repository;

import com.titan.commerce.modules.catalog.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    //pesquisa por categoria - id
    List<Product> findByCategoryId(Long categoryId);

}
