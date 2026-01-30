CREATE TABLE cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    cart_id VARCHAR(255) NOT NULL,
    product_variant_id BIGINT NOT NULL,

    quantity INT NOT NULL CHECK (quantity > 0),

    unit_price DECIMAL(10,2) NOT NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_cart_items_cart
        FOREIGN KEY (cart_id)
        REFERENCES carts(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_cart_items_product_variant
        FOREIGN KEY (product_variant_id)
        REFERENCES product_variants(id)
        ON DELETE RESTRICT,

    CONSTRAINT uq_cart_variant
        UNIQUE (cart_id, product_variant_id)
);

CREATE INDEX idx_cart_items_cart_id ON cart_items(cart_id);
CREATE INDEX idx_cart_items_variant_id ON cart_items(product_variant_id);
