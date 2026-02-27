# 🛒 Titan Commerce API - E-commerce Backend Architecture

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white)

> Uma API RESTful desenvolvida para sustentar operações críticas de E-commerce, com foco em resolução de concorrência de estoque, consistência financeira e segurança de dados.

## 📌 Sobre o Projeto

Este projeto não é apenas um CRUD de produtos. Ele foi desenhado para resolver os desafios reais do domínio de varejo digital. A arquitetura foca na garantia das propriedades ACID do banco de dados, na prevenção de anomalias financeiras e na automação de processos de negócio (*autocura* do sistema).

Projeto desenvolvido como parte prática do curso de **Sistemas para Internet no IF Sertão PE (Campus Salgueiro)**.

## 🛠️ Stack Tecnológica

* **Core:** Java 17, Spring Boot 3, Spring Web, Validation.
* **Persistência:** Spring Data JPA (Hibernate), MySQL.
* **Database Migrations:** Flyway (Database as Code).
* **Segurança:** Spring Security, Auth0 Java JWT.
* **Boilerplate & Docs:** Lombok, Springdoc OpenAPI (Swagger UI).

---

## 🏗️ Decisões de Engenharia e Arquitetura

### 1. Prevenção de *Race Condition* (Pessimistic Locking)
Em cenários de alta concorrência (ex: *Black Friday*), múltiplas requisições simultâneas podem tentar comprar a última unidade de um SKU, gerando estoque negativo.
* **Solução:** Implementação de `Pessimistic Lock` (`SELECT FOR UPDATE`) a nível de repositório. O banco de dados aplica um bloqueio exclusivo na linha da variante do produto durante a transação de checkout, garantindo que a segunda requisição aguarde e seja rejeitada (Erro 400) caso o estoque zere, mantendo a integridade logística.

### 2. Autocura do Sistema (Workers Assíncronos)
Pedidos abandonados no status `PENDING_PAYMENT` retêm o estoque, gerando "falsos negativos" e perda de vendas.
* **Solução:** Desenvolvimento do `OrderCleanupService`, um *Worker* autônomo agendado com `@Scheduled`. Ele varre o banco assincronamente buscando pedidos expirados. Ao encontrá-los, executa um *rollback* transacional duplo: cancela a intenção de pagamento e devolve a unidade exata ao estoque físico do SKU.

### 3. Modelagem de Dados: Volatilidade vs. Imutabilidade Histórica
Separação estrita entre intenção de compra e contrato financeiro.
* **O Carrinho (Volátil):** Configurado com `Orphan Removal` na JPA para deleção física de itens removidos, evitando acúmulo de lixo transacional e degradação de performance do banco.
* **O Pedido (Imutável):** Geração de um *Snapshot* financeiro (`unit_price`) no momento do checkout. Se o catálogo for alterado amanhã, o histórico de compras do usuário permanece inalterado.

```mermaid
erDiagram
    CART ||--o{ CART_ITEM : "1:N (Orphan Removal)"
    ORDER ||--o{ ORDER_ITEM : "1:N (Snapshot Imutável)"
    ORDER_ITEM {
        decimal unit_price "Preço congelado"
    }
