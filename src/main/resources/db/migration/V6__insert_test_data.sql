-- V6__insert_test_data.sql
-- Inserção de dados básicos para testes (Seed Data)

-- 1. Inserir ROLES
INSERT INTO roles (id, authority) VALUES (1, 'ROLE_USER') ON CONFLICT DO NOTHING;
INSERT INTO roles (id, authority) VALUES (2, 'ROLE_ADMIN') ON CONFLICT DO NOTHING;

-- 2. Inserir CATEGORIAS
INSERT INTO categories (name, slug, active, created_at)
VALUES ('Eletronicos', 'eletronicos', true, NOW());

INSERT INTO categories (name, slug, active, created_at)
VALUES ('Vestuario', 'vestuario', true, NOW());

-- 3. Inserir PRODUTOS (Pai)
INSERT INTO products (title, brand, description, category_id, active, created_at)
VALUES ('Smartphone Titan X', 'TitanTech', 'O melhor celular do mercado com 128GB.', 1, true, NOW());

INSERT INTO products (title, brand, description, category_id, active, created_at)
VALUES ('Camiseta Developer', 'DevWear', '100% Algodao, perfeita para codar.', 2, true, NOW());

-- 4. Inserir VARIAÇÕES
INSERT INTO product_variants (product_id, sku_code, price, stock_quantity, attributes, active)
VALUES (1, 'SMART-TITAN-BLK', 2500.00, 50, '{"cor": "Preto", "memoria": "128GB"}', true);

INSERT INTO product_variants (product_id, sku_code, price, stock_quantity, attributes, active)
VALUES (1, 'SMART-TITAN-WHT', 2500.00, 30, '{"cor": "Branco", "memoria": "128GB"}', true);

INSERT INTO product_variants (product_id, sku_code, price, stock_quantity, attributes, active)
VALUES (2, 'TSHIRT-DEV-M', 59.90, 100, '{"tamanho": "M", "cor": "Preta"}', true);