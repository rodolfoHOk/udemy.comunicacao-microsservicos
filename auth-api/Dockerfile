FROM node:16-alpine
WORKDIR .
COPY package*.json ./
RUN yarn
COPY . .
RUN yarn build
EXPOSE 8080
CMD ["node", "dist/app.js"]