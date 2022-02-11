import { Router } from 'express';

import OrderController from '../controller/OrderController';
import { checkToken } from '../../../config/auth/checkToken';

const orderRouter: Router = Router();

orderRouter.get('/', checkToken, OrderController.findAll);
orderRouter.get('/:id', checkToken, OrderController.findById);
orderRouter.post('/create', checkToken, OrderController.createOrder);
orderRouter.get(
  '/products/:productId',
  checkToken,
  OrderController.findByProductId
);

export default orderRouter;
