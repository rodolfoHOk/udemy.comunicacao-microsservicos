# Product API do Curso Comunicações entre Microsserviços

## Tecnologias 

- Java 11

- Spring Boot

- REST API

- PostgreSQL

- RabbitMQ

- JWT

- Spring Cloud OpenFeign

- Docker

### Bibliotecas

- Spring WEB

- Spring Data JPA

- PostgreSQL Driver

- Spring AMQP (RabbitMQ)

- Spring Cloud OpenFeign

- Spring DevTools

- Lombok

- Spring OAuth2 Resource Server

- Spring Validation

- SpringFox (OpenAPI)

- Awaitility (Integration Test)

- RestAssured (Integration Test)

## Resumo da implementação do Micro-serviço de Produtos:

- Criação do projeto Product API com Java, Spring Boot e PostgreSQL

- Criação do Dockerfile do projeto de Product API

- Configurando o PostgreSQL na aplicação

- Criando as entidades de Produto, Fornecedor e Categoria

- Inserindo os dados iniciais

- Criando estrutura inicial de DTO, Repository, Service e Controller de Categoria

- Criando método POST para Produto e Fornecedor

- Testando POSTs criados e adicionando buscas com métodos GET para Categoria

- Criando endpoints com métodos GET para Produto e Fornecedores

- Finalizando nosso CRUD com métodos PUT (atualização) e DELETE (remoção)

- Configurando o JWT no projeto

- Adicionando interceptador de token JWT nos endpoints

- Configurando o RabbitMQ

- Adicionando os Listener da fila de atualização do estoque do produto

- Atualizando o estoque e publicando (sender) na fila de confirmação de vendas

- Correção de lógica no processoa assíncrono

- Configurando client HTTP para Sales API com Feign Client

- Implementando último endpoint da aplicação. Verificar estoque dos produtos.

- Implementando tracing das requisições entre servicos no product api

- Refatorando a árvore de estrutura de arquivos

- Refatorando para usar o spring validation and ExceptionHandler atualizado

- Adicionando documentação OpenApi

- Adicionando testes unitarios para category service, supplier service e product service

- Adicionando testes unitarios para category controller, supplier controller e product controller

- Adicionando testes de integração para product stock listener, sales confirmation sender e sales client

- Definindo as variáveis de ambiente nas aplicações

## Links

[Repositório dos Projetos do Curso](https://github.com/rodolfoHOk/udemy.comunicacao-microsservicos)
