package com.titan.commerce.modules.cart.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.titan.commerce.modules.catalog.domain.ProductVariant;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_variant_id") // Mapeia a mesma coluna do banco
    private ProductVariant productVariant;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal price;

    public BigDecimal getSubTotal() {
        return this.price.multiply(BigDecimal.valueOf(this.quantity));
    }
}