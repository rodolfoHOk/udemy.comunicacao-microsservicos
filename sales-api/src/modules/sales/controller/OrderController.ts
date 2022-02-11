import { Request, Response } from 'express';
import { Document, Types } from 'mongoose';
import { OK } from '../../../config/constants/httpStatus';
import { IOrder, IProduct, IUser, OrderDocumentProps } from '../model/Order';
import OrderService from '../service/OrderService';

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

class OrderController {
  async createOrder(
    req: CreateOrderRequest,
    res: Response<OrderResponse | Problem>
  ) {
    let order = await OrderService.createOrder(req);
    return res.status(OK).json(order);
  }

  async findById(req: GetOrderRequest, res: Response<OrderResponse | Problem>) {
    let order = await OrderService.findById(req);
    return res.status(OK).json(order);
  }
}

export default new OrderController();
