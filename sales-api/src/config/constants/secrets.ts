const env = process.env;

export const MONGO_DB_URL = env.MONGO_DB_URL
  ? env.MONGO_DB_URL
  : 'mongodb://admin:mongodb@localhost:27017';

export const API_SECRET = env.API_SECRET
  ? env.API_SECRET
  : 'Y3Vyc28tY29tdW5pY2FjYW8tZW50cmUtbWljcm9zc2Vydmljb3MtYXV0aC1hcGktZGV2';

export const RABBIT_MQ_URL = env.RABBIT_MQ_URL
  ? env.RABBIT_MQ_URL
  : 'amqp://localhost:5672';
