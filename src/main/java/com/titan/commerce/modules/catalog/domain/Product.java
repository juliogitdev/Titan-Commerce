package com.titan.commerce.modules.catalog.domain;

import com.titan.commerce.modules.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name="products")
@Data
@EqualsAndHashCode(callSuper = true)

public class Product extends BaseEntity {

    @Column(nullable = false)
    private String title;

    private String description;

    private String brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private Category category;

}
