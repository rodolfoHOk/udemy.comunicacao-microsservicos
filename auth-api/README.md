# Authorization API do Curso Cominicações entre Microsserviços

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
  "typeRoots": ["./src/@types", "node_modules/@types"]
  }

- adicionar no package.json:

  "type": "module",
  "scripts": {
  "dev": "ts-node-dev --exit-child src/app.ts"
  },
