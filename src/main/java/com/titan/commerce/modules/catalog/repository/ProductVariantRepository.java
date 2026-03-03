package com.titan.commerce.modules.catalog.repository;

import com.titan.commerce.modules.catalog.domain.ProductVariant;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    //Busca pelo código sku do produto
    Optional<ProductVariant> findBySkuCode(String skuCode);

    //Verifica se existe o código sku existe
    boolean existsBySkuCode(String skuCode);

    //lista todas as variantes de um produto ativo
    List<ProductVariant> findByProductIdAndActiveTrue(Long productId);

    //lista todas as variantes ativa
    List<ProductVariant> findByActiveTrue();

    //lista todas as variantes desativadas (excluida)
    List<ProductVariant> findByActiveFalse();

    //Busca produto ativo pelo id
    Optional<ProductVariant> findByIdAndActiveTrue(Long id);

    // Esse
    // até a transação do checkout terminar (commit ou rollback).
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM ProductVariant p WHERE p.id = :id")
    Optional<ProductVariant> findByIdWithLock(@Param("id") Long id);


}
