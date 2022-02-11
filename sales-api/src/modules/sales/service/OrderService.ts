import { Request } from 'express';
import { Document, Types } from 'mongoose';

import { PENDING } from '../status/OrderStatus';
import {
  BAD_REQUEST,
  INTERNAL_SERVER_ERROR,
  OK,
  NOT_FOUND,
} from '../../../config/constants/httpStatus';

import OrderRepository from '../repository/OrderRepository';
import OrderException from '../exception/OrderException';
import { IOrder, IProduct, IUser, OrderDocumentProps } from '../model/Order';
import ProductClient from '../../product/client/ProductClient';
import {
  ProductStockUpdateMessage,
  sendMessageToProductStockUpdateQueue,
} from '../../product/rabbitmq/productStockUpdateSender';

type OrderType = Document<unknown, any, IOrder> &
  IOrder & { _id: Types.ObjectId } & OrderDocumentProps;

interface OrderResponse {
  status: number;
  order: OrderType;
}

interface Problem {
  status: number;
  message: string;
}

interface OrderData {
  products: IProduct[];
}

interface CreateOrderRequest extends Request<{}, {}, OrderData> {
  authUser: IUser;
}

interface GetOrderRequest extends Request {
  authUser: IUser;
}

interface OrderMessage {
  salesId: string;
  status: string;
}

class OrderService {
  async createOrder(req: CreateOrderRequest): Promise<OrderResponse | Problem> {
    try {
      let orderData = req.body;
      this.validateOrderData(orderData);
      const { authorization } = req.headers;
      await this.validateOrderStock(orderData, authorization);
      const { authUser } = req;

      let savedOrder = await this.saveOrder(orderData, authUser);
      this.sendMessage(savedOrder);

      return {
        status: OK,
        order: savedOrder,
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

  async findById(req: GetOrderRequest) {
    const { id } = req.params;
    try {
      this.validateInformedId(id);

      const order = await OrderRepository.findById(id);

      if (!order) {
        throw new OrderException(NOT_FOUND, 'The order was not found');
      }

      return {
        status: OK,
        order,
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

  async validateOrderStock(data: OrderData, bearerToken: string) {
    let stockIsOk = await ProductClient.checkProductStock(data, bearerToken);
    if (!stockIsOk) {
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

  validateInformedId(id: string) {
    if (!id) {
      throw new OrderException(BAD_REQUEST, 'The order id must be informed');
    }
  }
}

export default new OrderService();
