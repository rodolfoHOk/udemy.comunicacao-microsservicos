import express, { Express } from 'express';

import { connectMongoDB } from './config/db/mongoDbConfig';
import { createInitialData } from './config/db/initialData';
import { connectRabbitMq } from './config/rabbitmq/rabbitConfig';
import orderRouter from './modules/sales/routes/OrderRouter';
import tracing from './config/tracing';

const env = process.env;
const PORT = env.PORT || 8082;

const app: Express = express();

connectMongoDB();
createInitialData();
connectRabbitMq();

app.use(express.json());

app.get('/api/status', async (req, res) => {
  res.status(200).json({
    service: 'Sales-API',
    status: 'up',
    httpStatus: 200,
  });
});

app.use(tracing);

app.use('/api/orders', orderRouter);

app.listen(PORT, () => {
  console.info(`Server started successfully at port ${PORT}`);
});
