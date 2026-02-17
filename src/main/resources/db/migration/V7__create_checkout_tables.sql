-- 1. Tabela de Pedidos (Orders)
CREATE TABLE orders (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    shipping_addresses_id BIGINT NOT NULL,

    total_amount DECIMAL(19, 2) NOT NULL,

    status VARCHAR(50) NOT NULL, -- PENDING, PAID, CANCELED, EXPIRED

    expires_at TIMESTAMP, -- Hora limite para pagar antes de perder a reserva
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_orders_addresses FOREIGN KEY (shipping_addresses_id) REFERENCES addresses(id)
);

-- 2. Itens do Pedido (Imutável)
CREATE TABLE order_items (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    order_id VARCHAR(255) NOT NULL,
    product_variant_id BIGINT NOT NULL,

    quantity INT NOT NULL,
    unit_price DECIMAL(19, 2) NOT NULL, -- Preço CONGELADO no momento da compra
    sub_total DECIMAL(19, 2) NOT NULL,

    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_order_items_variant FOREIGN KEY (product_variant_id) REFERENCES product_variants(id)
);

-- 3. Pagamentos
CREATE TABLE payments (
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    order_id VARCHAR(255) NOT NULL,

    payment_method VARCHAR(50) NOT NULL, -- PIX, CREDIT_CARD
    transaction_id VARCHAR(255), -- ID do Gateway (Stripe/MercadoPago)

    status VARCHAR(50) NOT NULL, -- PENDING, APPROVED, FAILED
    paid_at TIMESTAMP,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payments_order FOREIGN KEY (order_id) REFERENCES orders(id)
);

-- Índices para performance (Buscas rápidas)
CREATE INDEX idx_orders_user ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(status);