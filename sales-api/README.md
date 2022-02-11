# Sales API do Curso Comunicações entre Microsserviços

## Resumo da implementação do Micro-serviço de Vendas:

- Criação do projeto Sales API com Node.js, Express.js, JWT e MongoDB

- Criação do Dockerfile do projeto de Sales API

- Configurando o MongoDB

- Criando as models do MongoDB

- Inserindo os dados iniciais

- Configurando interceptador para validação de token JWT na aplicação

- Configurando o RabbitMQ

- Criando configuração para aguardar o container do RabbitMQ subir

- Criando Listener da aplicação

- Criando Sender da aplicação

- Criando Repository e Service de um pedido

- Criando processamento assíncrono de recebimento de confirmação de uma venda

- Criando chamada HTTP REST para Product-API com Axios para validar estoque

- Criando Controller e Routes e endpoint GET por ID para os pedidos

- Finaliza endpoints GET e testes da comunicação entre as APIs com Postman / Insomnia

- Implementando tracing de requisições entre serviços em Sales-API

- Salvando IDs de tracing de requisições na model do MongoDB

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

## End
