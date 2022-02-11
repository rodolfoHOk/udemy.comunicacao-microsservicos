import { Request } from 'express';
import { Document, Types } from 'mongoose';
import { IncomingHttpHeaders } from 'http';

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

interface OrdersResponse {
  status: number;
  orders: OrderType[];
}

interface OrdersByProductIdResponse {
  status: number;
  salesIds: string[];
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
  headers: IncomingHttpHeaders & {
    transactionid?: string;
    serviceid?: string;
  };
}

interface GetOrderRequest extends Request {
  authUser: IUser;
  headers: IncomingHttpHeaders & {
    transactionid?: string;
    serviceid?: string;
  };
}

interface OrderMessage {
  salesId: string;
  status: string;
  transactionid: string;
}

class OrderService {
  async createOrder(req: CreateOrderRequest): Promise<OrderResponse | Problem> {
    try {
      const { transactionid, serviceid } = req.headers;
      console.info(
        `Request to POST orders/create with data ${JSON.stringify(
          req.body
        )} | [transactionid: ${transactionid} | serviceid: ${serviceid}]`
      );

      let orderData = req.body;
      this.validateOrderData(orderData);
      const { authorization } = req.headers;
      await this.validateOrderStock(orderData, authorization, transactionid);
      const { authUser } = req;

      let savedOrder = await this.saveOrder(
        orderData,
        authUser,
        transactionid,
        serviceid
      );
      this.sendMessage(savedOrder, transactionid);

      let response: OrderResponse = {
        status: OK,
        order: savedOrder,
      };

      console.info(
        `Response to POST orders/create with data ${JSON.stringify(
          response
        )} | [transactionid: ${transactionid} | serviceid: ${serviceid}]`
      );
      return response;
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
      if (order.salesId && order.status && order.transactionid) {
        let existingOrder = await OrderRepository.findById(order.salesId);
        if (existingOrder && order.status !== existingOrder.status) {
          existingOrder.status = order.status;
          existingOrder.updatedAt = new Date();
          await OrderRepository.save(existingOrder);
          console.info(
            `The order has updated with the order message data ${
              order.transactionid
                ? '[transactionid: ' + order.transactionid + ']'
                : ''
            }`
          );
        } else {
          console.warn(
            `The sales Id informed on order message not exist ${
              order.transactionid
                ? '[transactionid: ' + order.transactionid + ']'
                : ''
            }`
          );
        }
      } else {
        console.warn(
          `The order message was not complete ${
            order.transactionid
              ? '[transactionid: ' + order.transactionid + ']'
              : ''
          }`
        );
      }
    } catch (err) {
      console.error('Could not parse order message from queue');
      console.error(err.message);
    }
  }

  async findById(req: GetOrderRequest): Promise<OrderResponse | Problem> {
    try {
      const { transactionid, serviceid } = req.headers;
      console.info(
        `Request to GET orders/:id with params ${JSON.stringify(
          req.params
        )} | [transactionid: ${transactionid} | serviceid: ${serviceid}]`
      );

      const { id } = req.params;
      this.validateInformedId(id);
      const order = await OrderRepository.findById(id);
      if (!order) {
        throw new OrderException(NOT_FOUND, 'The order was not found');
      }
      let response: OrderResponse = {
        status: OK,
        order,
      };

      console.info(
        `Response to GET orders/:id with data ${JSON.stringify(
          response
        )} | [transactionid: ${transactionid} | serviceid: ${serviceid}]`
      );
      return response;
    } catch (err) {
      return {
        status: err.status ? err.status : INTERNAL_SERVER_ERROR,
        message: err.message,
      };
    }
  }

  async findAll(req: GetOrderRequest): Promise<OrdersResponse | Problem> {
    try {
      const { transactionid, serviceid } = req.headers;
      console.info(
        `Request to GET orders with params ${JSON.stringify(
          req.params
        )} | [transactionid: ${transactionid} | serviceid: ${serviceid}]`
      );

      const orders = await OrderRepository.findAll();
      if (!orders) {
        throw new OrderException(NOT_FOUND, 'No orders were found');
      }
      let response: OrdersResponse = {
        status: OK,
        orders,
      };

      console.info(
        `Response to GET orders with data ${JSON.stringify(
          response
        )} | [transactionid: ${transactionid} | serviceid: ${serviceid}]`
      );
      return response;
    } catch (err) {
      return {
        status: err.status ? err.status : INTERNAL_SERVER_ERROR,
        message: err.message,
      };
    }
  }

  async findByProductId(
    req: GetOrderRequest
  ): Promise<OrdersByProductIdResponse | Problem> {
    try {
      const { transactionid, serviceid } = req.headers;
      console.info(
        `Request to GET orders/products/:productId with params ${JSON.stringify(
          req.params
        )} | [transactionid: ${transactionid} | serviceid: ${serviceid}]`
      );

      const { productId } = req.params;
      this.validateInformedProductId(productId);
      const orders = await OrderRepository.findByProductId(productId);
      if (!orders) {
        throw new OrderException(
          NOT_FOUND,
          'No orders were found with product id informed'
        );
      }
      let response: OrdersByProductIdResponse = {
        status: OK,
        salesIds: orders.map((order) => order._id.toString()),
      };

      console.info(
        `Response to GET orders/products/:productId with data ${JSON.stringify(
          response
        )} | [transactionid: ${transactionid} | serviceid: ${serviceid}]`
      );
      return response;
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

  async validateOrderStock(
    data: OrderData,
    bearerToken: string,
    transactionid: string
  ) {
    let stockIsOk = await ProductClient.checkProductStock(
      data,
      bearerToken,
      transactionid
    );
    if (!stockIsOk) {
      throw new OrderException(
        BAD_REQUEST,
        'The stock is out for the products'
      );
    }
  }

  async saveOrder(
    orderData: OrderData,
    authUser: IUser,
    transactionid: string,
    serviceid: string
  ): Promise<OrderType> {
    let order: IOrder = {
      status: PENDING,
      user: authUser,
      createdAt: new Date(),
      updatedAt: new Date(),
      products: orderData.products,
      transactionid,
      serviceid,
    };
    return await OrderRepository.save(order);
  }

  sendMessage(orderCreated: OrderType, transactionid: string) {
    let message: ProductStockUpdateMessage = {
      salesId: orderCreated._id.toString(),
      products: orderCreated.products,
      transactionid,
    };
    sendMessageToProductStockUpdateQueue(message, transactionid);
  }

  validateInformedId(id: string) {
    if (!id) {
      throw new OrderException(BAD_REQUEST, 'The order id must be informed');
    }
  }

  validateInformedProductId(productId: string) {
    if (!productId) {
      throw new OrderException(
        BAD_REQUEST,
        'The order product id must be informed'
      );
    }
  }
}

export default new OrderService();
