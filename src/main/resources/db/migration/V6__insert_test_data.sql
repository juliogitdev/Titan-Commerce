-- V6__insert_test_data.sql
-- Inserção de dados básicos para testes (Seed Data)

-- 1. Inserir ROLES
INSERT IGNORE INTO roles (id, authority) VALUES (1, 'ROLE_USER');
INSERT IGNORE INTO roles (id, authority) VALUES (2, 'ROLE_ADMIN');

-- 2. Inserir CATEGORIAS
INSERT INTO categories (name, slug, active, created_at)
VALUES ('Eletrônicos', 'eletronicos', true, NOW());

INSERT INTO categories (name, slug, active, created_at)
VALUES ('Vestuário', 'vestuario', true, NOW());

-- 3. Inserir PRODUTOS (Pai)
-- Produto 1: Smartphone (Categoria 1)
INSERT INTO products (title, brand, description, category_id, active, created_at)
VALUES ('Smartphone Titan X', 'TitanTech', 'O melhor celular do mercado com 128GB.', 1, true, NOW());

-- Produto 2: Camiseta (Categoria 2)
INSERT INTO products (title, brand, description, category_id, active, created_at)
VALUES ('Camiseta Developer', 'DevWear', '100% Algodão, perfeita para codar.', 2, true, NOW());

-- 4. Inserir VARIAÇÕES (IMPORTANTE:

-- Variação ID 1: Smartphone Preto
INSERT INTO product_variants (product_id, sku_code, price, stock_quantity, attributes, active, created_at)
VALUES (1, 'SMART-TITAN-BLK', 2500.00, 50, '{"cor": "Preto", "memoria": "128GB"}', true, NOW());

-- Variação ID 2: Smartphone Branco
INSERT INTO product_variants (product_id, sku_code, price, stock_quantity, attributes, active, created_at)
VALUES (1, 'SMART-TITAN-WHT', 2500.00, 30, '{"cor": "Branco", "memoria": "128GB"}', true, NOW());

-- Variação ID 3: Camiseta M
INSERT INTO product_variants (product_id, sku_code, price, stock_quantity, attributes, active, created_at)
VALUES (2, 'TSHIRT-DEV-M', 59.90, 100, '{"tamanho": "M", "cor": "Preta"}', true, NOW());