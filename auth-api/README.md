# Authorization API do Curso Cominicações entre Microsserviços

## Tecnologias

- TypeScript

- NodeJS

- Express

- Sequelize

- REST API

- PostgreSQL

- JWT

- Docker

### Bibliotecas

- bcrypt

- cors

- express

- jsonwebtoken

- pg (PostgreSQL)

- sequelize

- uuid

- typescript (desenvolvimento)

- ts-node-dev (desenvolvimento)

- @types/bcrypt, @types/cors, @types/express, @types/jsonwebtoken, @types/node, @types/pg, @types/uuid (desenvolvimento)

## Resumo da implementação do Micro-serviço de Autenticação:

- Criação do projeto Auth API com Node.js, Express.js, JWT e PostgreSQL

- Criação do Dockerfile do projeto de Auth API

- Configurando o Sequelize no projeto

- Criando os dados inicial do serviço

- Dividindo a aplicação em camadas

- Criando o repository e a service

- Criando exceptions e as camadas de controller e router

- Desenvolvendo a autenticação com JWT

- Adicionando o middleware de autenticação

- Implementando segurança nos endpoints

- Implementando tracing de requisições entre serviços em Auth-API

- Definindo variáveis de ambiente nas aplicações

- - Últimos ajustes antes de subir tudo integrado

### Criar/iniciar uma aplicação em node com Typescript

- yarn init -y

- yarn add typescript ts-node-dev @types/node tsconfig-paths -D

- npx tsc --init

- deixar tsconfig.json assim:

        {
          "compilerOptions": {
            "target": "es2017",
            "module": "commonjs",
            "esModuleInterop": true,
            "forceConsistentCasingInFileNames": true,
            "strict": false,
            "skipLibCheck": true,
            "rootDir": "src",
            "outDir": "dist",
            "typeRoots": ["./src/@types", "node_modules/@types"]
          }
        }

- adicionar no package.json:

        {
          .
          .
          .
          "type": "module",
          "scripts": {
            "dev": "ts-node-dev --exit-child src/app.ts",
            "build": "tsc --build"
          },
          .
          .
          .
        }

- adicionar .gitignore para node

## Links

[Repositório dos Projetos do Curso](https://github.com/rodolfoHOk/udemy.comunicacao-microsservicos)
