import express, { Express } from 'express';

import { connectMongoDB } from './config/db/mongoDbConfig';
import { createInitialData } from './config/db/initialData';
import Order from './modules/sales/model/Order';
import { checkToken } from './config/auth/checkToken';
import { connectRabbitMq } from './config/rabbitmq/rabbitConfig';
import { sendMessageToProductStockUpdate } from './modules/product/rabbitmq/productStockUpdateSender';

const env = process.env;
const PORT = env.PORT || 8082;

const app: Express = express();

connectMongoDB();
createInitialData();
connectRabbitMq();

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

app.get('/api/test', checkToken, async (req, res) => {
  try {
    sendMessageToProductStockUpdate({
      salesId: 'dve45815ber1561tv65er1t6',
      products: [
        {
          productId: 1001,
          quantity: 2,
        },
        {
          productId: 1002,
          quantity: 1,
        },
        {
          productId: 1003,
          quantity: 1,
        },
      ],
    });
    return res.status(200).json({ status: 200, message: 'Success' });
  } catch (err) {
    console.error(err.message);
    return res.status(500).json({ status: 500, message: err.message });
  }
});

app.listen(PORT, () => {
  console.info(`Server started successfully at port ${PORT}`);
});
