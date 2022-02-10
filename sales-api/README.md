# Sales API do Curso Cominicações entre Microsserviços

## Resumo da implementação do Micro-serviço de Vendas:

- Criação do projeto Sales API com Node.js, Express.js, JWT e MongoDB

- Criação do Dockerfile do projeto de Sales API

- Configurando o MongoDB

- Criando as models do MongoDB

-

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
