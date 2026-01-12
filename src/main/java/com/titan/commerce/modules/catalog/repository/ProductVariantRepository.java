package com.titan.commerce.modules.catalog.repository;

import com.titan.commerce.modules.catalog.domain.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    //Busca pelo código sku do produto
    Optional<ProductVariant> findBySkuCode(String skuCode);

    //Verifica se existe o código sku existe
    boolean existsBySkuCode(String skuCode);

}
