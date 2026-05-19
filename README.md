# 🛒 SecureMarket API

O **SecureMarket** é uma API REST para um ecossistema de Marketplace completo e seguro. O sistema gerencia de ponta a ponta o cadastro e autenticação de usuários, abertura de lojas, controle transacional de estoque e o processamento de pedidos de compras.

O projeto foi totalmente estruturado seguindo os padrões de mercado com a arquitetura em camadas (**Controller, Service, Repository, DTO**).

---

## 🏗️ Arquitetura do Banco de Dados (Mapeamento Relacional)

A API foi modelada com base no seguinte design de banco de dados (ERD):

* **Users ➡️ Sellers (`@OneToOne`):** Um usuário pode possuir no máximo uma única loja (perfil vendedor).
* **Sellers ➡️ Products (`@OneToMany`):** Uma loja pode ter vários produtos cadastrados, mas cada produto pertence a uma única loja.
* **Users ➡️ Orders (`@ManyToOne`):** Um cliente pode realizar múltiplos pedidos no ecossistema.
* **Orders ➡️ OrderItems ➡️ Products:** Relacionamento gerenciado para decompor os itens do carrinho de compras, travando o histórico de preços (`unit_price`) e garantindo o controle rígido do estoque.

---

## 🛠️ Tecnologias Utilizadas

* **Java 21 / 25** (Pronto para os recursos mais modernos da linguagem)
* **Spring Boot 3.5.x** (Core da aplicação)
* **Spring Security & JWT (Json Web Token)** (Autenticação baseada em tokens com controle de rotas por Roles)
* **Spring Data JPA & Hibernate** (Persistência, ORM e geração automática de tabelas)
* **PostgreSQL** (Banco de Dados Relacional)
* **Docker & Docker Compose** (Containerização e orquestração do ambiente)
* **Lombok** (Redução de código boilerplate)
* **Bean Validation** (Validação de dados de entrada)

---

## 🛡️ Funcionalidades Principais

1.  **Autenticação Avançada:** Cadastro de usuários com senhas criptografadas (`BCrypt`) e geração de tokens JWT.
2.  **Segurança de Contexto:** Vinculação automática de lojas e produtos recuperando o usuário autenticado direto do Token, mitigando falhas de segurança (ID Spoofing).
3.  **Processamento Transacional (`@Transactional`):** Cálculo automático de valores, baixa automatizada de estoque e garantia de integridade (Rollback em lote caso falte estoque em qualquer item do carrinho).
4.  **Histórico de Pedidos:** Rota exclusiva para o cliente logado consultar seu histórico de compras de forma isolada e segura.

---

## 🐳 Como Rodar o Projeto (Praticidade com Docker)

Você **não precisa** ter o Java nem o PostgreSQL instalados fisicamente na sua máquina. Basta ter o **Docker** e o **Docker Compose** configurados.


   ### Passo a Passo
1. Clone o repositório:
   ```bash
   git clone [https://github.com/brunoTelesDev/securemarket.git](https://github.com/brunoTelesDev/securemarket.git)
