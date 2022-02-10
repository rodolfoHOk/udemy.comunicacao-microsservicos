const env = process.env;

export const MONGO_DB_URL = env.MONGO_DB_URL
  ? env.MONGO_DB_URL
  : 'mongodb://admin:mongodb@localhost:27017';
