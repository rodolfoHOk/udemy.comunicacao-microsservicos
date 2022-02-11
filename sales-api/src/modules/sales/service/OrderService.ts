import { Request } from 'express';

import OrderRepository from '../repository/OrderRepository';
import OrderException from '../exception/OrderException';
import {
  ProductStockUpdateMessage,
  sendMessageToProductStockUpdateQueue,
} from '../../product/rabbitmq/productStockUpdateSender';
import { PENDING, APPROVED, REJECTED } from '../status/OrderStatus';
import { IOrder, IProduct, IUser, OrderDocumentProps } from '../model/Order';
import {
  BAD_REQUEST,
  INTERNAL_SERVER_ERROR,
  OK,
} from '../../../config/constants/httpStatus';
import { Document, Types } from 'mongoose';

interface OrderResponse {
  status: number;
  order: OrderType;
}

export type OrderType = Document<unknown, any, IOrder> &
  IOrder & { _id: Types.ObjectId } & OrderDocumentProps;

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

interface OrderMessage {
  salesId: string;
  status: string;
}

class OrderService {
  async createOrder(
    req: AuthUserInfoRequest
  ): Promise<OrderResponse | Problem> {
    try {
      const { authUser } = req;
      let orderData = req.body;
      this.validateOrderData(orderData);
      this.validateOrderStock(orderData);

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

  async updateOrder(orderMessage: string) {
    try {
      const order: OrderMessage = JSON.parse(orderMessage);
      if (order.salesId && order.status) {
        let existingOrder = await OrderRepository.findById(order.salesId);
        if (existingOrder && order.status !== existingOrder.status) {
          existingOrder.status = order.status;
          await OrderRepository.save(existingOrder);
        } else {
          console.warn('The sales Id informed on order message not exist');
        }
      } else {
        console.warn('The order message was not complete');
      }
    } catch (err) {
      console.error('Could not parse order message from queue');
      console.error(err.message);
    }
  }

  validateOrderData(data: OrderData) {
    if (!data || !data.products) {
      throw new OrderException(BAD_REQUEST, 'The products must be informed');
    }
  }

  async validateOrderStock(data: OrderData) {
    // todo: request for product api
    let stockIsOut = true; // todo: depends of request
    if (stockIsOut) {
      throw new OrderException(
        BAD_REQUEST,
        'The stock is out for the products'
      );
    }
  }
}

export default new OrderService();
