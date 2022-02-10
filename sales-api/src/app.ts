import express, { Express } from 'express';

import { connect } from './config/db/mongoDbConfig';
import { createInitialData } from './config/db/initialData';
import Order from './modules/sales/model/Order';
import { checkToken } from './config/auth/checkToken';

const env = process.env;
const PORT = env.PORT || 8082;

const app: Express = express();

connect();
createInitialData();

app.get('/api/status', async (req, res) => {
  res.status(200).json({
    service: 'Sales-API',
    status: 'up',
    httpStatus: 200,
  });
});

app.get('/api/all', checkToken, async (req, res) => {
  try {
    res.status(200).json(await Order.find());
  } catch (err) {
    console.error(err.message);
  }
});

app.listen(PORT, () => {
  console.info(`Server started successfully at port ${PORT}`);
});
