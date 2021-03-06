# Curso Comunicacao entre Microsservicos - Udemy

## Resumo do Curso

- Comunicação síncrona entre serviços utilizando chamadas de API REST

- Comunicação assíncrona entre serviços utilizando AMQP com RabbitMQ e fila de mensagens

- Criação de containers para aplicações, bancos de dados e comunicação

- Criação de APIs utilizando Java com Spring Boot e PostgreSQL

- Criação de APIs utilizando Javascript ES6, Nodejs, Expressjs e MongoDB

- Comunicação entre containers utilizando Docker-Compose

- Como realizar o deploy de microsserviços no Heroku

- Como proteger sua aplicação com variáveis de ambiente

## Informações sobre os Projetos do Curso

[Auth-API](auth-api/README.md)

[Product-API](product-api/README.md)

[Sales-API](sales-api/README.md)

## Deploy

- Auth-API : API em NodeJs -> Heroku | PostgreSQL -> Heroku | Logs -> Heroku Coralogix Kibana

		https://curso-com-mserv-auth-api.herokuapp.com

- Product-API : API em Java -> Heroku | PostgreSQL -> Heroku | RabbitMQ -> CloudAMQP | Logs -> Heroku Coralogix Kibana

		https://curso-com-mserv-product-api.herokuapp.com/swagger-ui/index.html

- Sales-API : API em NodeJs -> Heroku | MongoDB -> Atlas MongoDB | RabbitMQ -> CloudAMQP | Logs -> Heroku Coralogix Kibana

		https://curso-com-mserv-sales-api.herokuapp.com

## Implementações além do curso

### Auth-api e Sales-api em NodeJS

- Typescript

### Product-api em Java:

- DDD

- Spring Validation

- OpenApi (SpringFox)

- Testes Unitarios (classes de serviços e controller)

- Testes de Integração (RabbitMQ Listener, RabbitMQ Sender e Feign Client)  

## Docker containers

- Criar container do banco de dados de autenticação (PostgreSQL):

		docker run --name auth-db -p 5432:5432 -e POSTGRES_DB=auth-db -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=123456 -d postgres

- Criar container do banco de dados de produtos (PostgreSQL):

		docker run --name product-db -p 5433:5432 -e POSTGRES_DB=product-db -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=123456 -d postgres

- Criar container do pgadmin4 para gerenciar os bancos de dados PostgreSQL:

		docker run --name pgadmin -p 5050:80 -e "PGADMIN_DEFAULT_EMAIL=admin@domain.com" -e "PGADMIN_DEFAULT_PASSWORD=SuperSecret" -d dpage/pgadmin4

- Criar container do banco de dados de vendas (Mongo DB):

		docker run --name sales-db -p 27017:27017 -e MONGO_INITDB_DATABASE=sales-db -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=123456 -d mongo:4


- Acessar o Mongo DB para gerenciar o banco de dados:

		docker exec -it sales-db bash --depois--> mongo admin -u admin -p '123456'
		
- Criar container do Message Broker (RabbitMQ):

		docker run --name sales-rabbit -p 5672:5672 -p 25676:25676 -p 15672:15672 -d rabbitmq:3-management
