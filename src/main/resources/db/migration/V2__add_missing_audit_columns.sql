-- V2: Ajusta colunas de auditoria (Created/Updated) para alinhar com BaseEntity.java

-- 1. Adicionando created_at onde faltou (com valor padrão para não quebrar dados existentes)
ALTER TABLE addresses ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE product_variants ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- 2. Adicionando updated_at em TODAS as tabelas (inicialmente nulo)
ALTER TABLE users ADD COLUMN updated_at TIMESTAMP;
ALTER TABLE addresses ADD COLUMN updated_at TIMESTAMP;
ALTER TABLE categories ADD COLUMN updated_at TIMESTAMP;
ALTER TABLE products ADD COLUMN updated_at TIMESTAMP;
ALTER TABLE product_variants ADD COLUMN updated_at TIMESTAMP;


-- 3. Adicionatando campo active para endereços
ALTER TABLE addresses ADD COLUMN active BOOLEAN DEFAULT TRUE;