import { Request } from 'express';
import { Document, Types } from 'mongoose';

import { PENDING } from '../status/OrderStatus';
import {
  BAD_REQUEST,
  INTERNAL_SERVER_ERROR,
  OK,
} from '../../../config/constants/httpStatus';

import OrderRepository from '../repository/OrderRepository';
import OrderException from '../exception/OrderException';
import { IOrder, IProduct, IUser, OrderDocumentProps } from '../model/Order';
import ProductClient from '../../product/client/ProductClient';
import {
  ProductStockUpdateMessage,
  sendMessageToProductStockUpdateQueue,
} from '../../product/rabbitmq/productStockUpdateSender';

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
      let orderData = req.body;
      this.validateOrderData(orderData);
      const { authorization } = req.headers;
      this.validateOrderStock(orderData, authorization);
      const { authUser } = req;

      let orderCreated = await this.saveOrder(orderData, authUser);
      this.sendMessage(orderCreated);

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
          existingOrder.updatedAt = new Date();
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

  async validateOrderStock(data: OrderData, bearerToken: string) {
    let stockIsOut = await ProductClient.checkProductStock(data, bearerToken);
    if (stockIsOut) {
      throw new OrderException(
        BAD_REQUEST,
        'The stock is out for the products'
      );
    }
  }

  async saveOrder(orderData: OrderData, authUser: IUser): Promise<OrderType> {
    let order: IOrder = {
      status: PENDING,
      user: authUser,
      createdAt: new Date(),
      updatedAt: new Date(),
      products: orderData.products,
    };
    return await OrderRepository.save(order);
  }

  sendMessage(orderCreated: OrderType) {
    let message: ProductStockUpdateMessage = {
      salesId: orderCreated._id.toString(),
      products: orderCreated.products,
    };
    sendMessageToProductStockUpdateQueue(message);
  }
}

export default new OrderService();
