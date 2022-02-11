import { Router } from 'express';

import OrderController from '../controller/OrderController';
import { checkToken } from '../../../config/auth/checkToken';

const orderRouter: Router = Router();

orderRouter.get('/:id', checkToken, OrderController.findById);
orderRouter.post('/create', checkToken, OrderController.createOrder);

export default orderRouter;
