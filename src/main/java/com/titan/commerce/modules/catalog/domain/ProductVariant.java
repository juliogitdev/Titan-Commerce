package com.titan.commerce.modules.catalog.domain;

import com.titan.commerce.modules.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name= "product_variants")
@Data
@EqualsAndHashCode(callSuper = true)

public class ProductVariant extends BaseEntity {

    @JoinColumn(name="product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Product product;

    @Column(nullable = false, unique = true)
    private String skuCode;

    private Integer stockQuantity;
    private BigDecimal price;

    @Column(columnDefinition = "json")
    private String attributes;
}
