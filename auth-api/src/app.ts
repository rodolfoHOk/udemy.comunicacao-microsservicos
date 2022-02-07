import express, { Express } from 'express';
import * as db from './config/db/initialData';

const env = process.env;
const PORT = env.PORT || 8080;

const app: Express = express();

db.createInitialData();

app.get('/api/status', (req, res) => {
  return res.status(200).json({
    service: 'Auth-API',
    status: 'up',
    httpStatus: 200,
  });
});

app.listen(PORT, () => {
  console.info(`Server started successfully at port ${PORT}`);
});
