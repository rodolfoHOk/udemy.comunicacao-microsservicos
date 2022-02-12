# Product API do Curso Comunicações entre Microsserviços

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
