import { Schema, model, Types, Model } from 'mongoose';

export interface IProduct {
  productId: number;
  quantity: number;
}

export interface IUser {
  id: string;
  name: string;
  email: string;
}

export interface IOrder {
  products: IProduct[];
  user: IUser;
  status: string;
  createdAt: Date;
  updatedAt: Date;
  transactionid: string;
  serviceid: string;
}

export type OrderDocumentProps = {
  products: Types.DocumentArray<IProduct>;
  user: Types.Subdocument<Types.ObjectId> & IUser;
};

type OrderModelType = Model<IOrder, {}, OrderDocumentProps>;

const OrderSchema = new Schema<IOrder>({
  products: [
    new Schema<IProduct>({
      productId: { type: Number, required: true },
      quantity: { type: Number, required: true },
    }),
  ],
  user: new Schema<IUser>({
    name: { type: String, required: true },
    email: { type: String, required: true },
  }),
  status: { type: String, required: true },
  createdAt: { type: Date, required: true },
  updatedAt: { type: Date, required: true },
  transactionid: { type: String, required: true },
  serviceid: { type: String, required: true },
});

export default model<IOrder, OrderModelType>('Order', OrderSchema);
