package com.titan.commerce.modules.catalog.domain;

import com.titan.commerce.modules.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="categories")
@Data
@EqualsAndHashCode(callSuper = true)
public class Category extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @ToString.Exclude
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Category> children = new ArrayList<>();
}
