import Order, { IOrder } from '../model/Order';

class OrderRepository {
  async save(order: IOrder) {
    try {
      return await Order.create(order);
    } catch (err) {
      console.error(err.message);
      return null;
    }
  }

  async findById(id: string) {
    try {
      return await Order.findById(id);
    } catch (err) {
      console.error(err.message);
      return null;
    }
  }

  async findAll() {
    try {
      return await Order.find();
    } catch (err) {
      console.error(err.message);
      return null;
    }
  }
}

export default new OrderRepository();
