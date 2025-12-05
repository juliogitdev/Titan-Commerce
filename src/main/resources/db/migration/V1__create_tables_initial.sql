-- V1__create_tables_initial.sql

-- 1. Tabela de Usuários
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255)
);

-- 2. Tabela de Endereços
CREATE TABLE addresses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    zip_code VARCHAR(20),
    street VARCHAR(255),
    city VARCHAR(100),
    CONSTRAINT fk_user_address FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 3. Tabela de Categorias
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    parent_id BIGINT,
    CONSTRAINT fk_category_parent FOREIGN KEY (parent_id) REFERENCES categories(id)
);

-- 4. Tabela de Produtos (Genérico)
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE,
    title VARCHAR(255) NOT NULL,
    brand VARCHAR(100),
    description TEXT,
    category_id BIGINT,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- 5. Tabela de Variações (SKU) - Com JSON para atributos
CREATE TABLE product_variants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    sku_code VARCHAR(100) NOT NULL UNIQUE,
    stock_quantity INT DEFAULT 0,
    price DECIMAL(19, 2) NOT NULL,
    attributes JSON, -- JSON (Cor, Tamanho, Numero, etc)
    active BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_variant_product FOREIGN KEY (product_id) REFERENCES products(id)
);