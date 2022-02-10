import { Request } from 'express';

import OrderRepository from '../repository/OrderRepository';
import OrderException from '../exception/OrderException';
import {
  ProductStockUpdateMessage,
  sendMessageToProductStockUpdateQueue,
} from '../../product/rabbitmq/productStockUpdateSender';
import { PENDING } from '../status/OrderStatus';
import { IOrder, IProduct, IUser, OrderDocumentProps } from '../model/Order';
import {
  BAD_REQUEST,
  INTERNAL_SERVER_ERROR,
  OK,
} from '../../../config/constants/httpStatus';
import { Document, Types } from 'mongoose';

interface OrderResponse {
  status: number;
  order: Document<unknown, any, IOrder> &
    IOrder & { _id: Types.ObjectId } & OrderDocumentProps;
}

interface Problem {
  status: number;
  message: string;
}

interface OrderData {
  products: IProduct[];
}

interface AuthUserInfoRequest extends Request<{}, {}, OrderData> {
  authUser: IUser;
}

class OrderService {
  async createOrder(
    req: AuthUserInfoRequest
  ): Promise<OrderResponse | Problem> {
    try {
      const { authUser } = req;
      let orderData = req.body;
      this.validateOrderData(orderData);

      let order: IOrder = {
        status: PENDING,
        user: authUser,
        createdAt: new Date(),
        updatedAt: new Date(),
        products: orderData.products,
      };
      let orderCreated = await OrderRepository.save(order);

      let message: ProductStockUpdateMessage = {
        salesId: orderCreated._id.toString(),
        products: orderCreated.products,
      };
      sendMessageToProductStockUpdateQueue(message);

      return {
        status: OK,
        order: orderCreated,
      };
    } catch (err) {
      return {
        status: err.status ? err.status : INTERNAL_SERVER_ERROR,
        message: err.message,
      };
    }
  }

  validateOrderData(data: OrderData) {
    if (!data || !data.products) {
      throw new OrderException(BAD_REQUEST, 'The products must be informed');
    }
  }
}

export default new OrderService();
