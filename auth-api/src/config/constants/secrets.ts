const env = process.env;

export const API_SECRET = env.API_SECRET
  ? env.API_SECRET
  : 'Y3Vyc28tY29tdW5pY2FjYW8tZW50cmUtbWljcm9zc2Vydmljb3MtYXV0aC1hcGktZGV2';

export const DB_HOST = env.DB_HOST ? env.DB_HOST : 'localhost';

export const DB_PORT = env.DB_PORT ? env.DB_PORT : '5432';

export const DB_NAME = env.DB_NAME ? env.DB_NAME : 'auth';

export const DB_USER = env.DB_USER ? env.DB_USER : 'postgres';

export const DB_PASSWORD = env.DB_PASSWORD ? env.DB_PASSWORD : 'postgres';
