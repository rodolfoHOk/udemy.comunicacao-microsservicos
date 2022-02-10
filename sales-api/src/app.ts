import express, { Express } from 'express';

import { connect } from './config/db/mongoDbConfig';
import Order from './modules/sales/model/Order';

const env = process.env;
const PORT = env.PORT || 8082;

const app: Express = express();

connect();

app.get('/api/status', async (req, res) => {
  let test = await Order.find();
  console.log(test);
  res.status(200).json({
    service: 'Sales-API',
    status: 'up',
    httpStatus: 200,
  });
});

app.listen(PORT, () => {
  console.info(`Server started successfully at port ${PORT}`);
});
