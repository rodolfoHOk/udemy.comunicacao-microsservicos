import Order, { IOrder } from '../../modules/sales/model/Order';

export async function createInitialData() {
  await Order.collection.drop();

  const firstOrder: IOrder = {
    products: [
      {
        productId: 1001,
        quantity: 2,
      },
      {
        productId: 1002,
        quantity: 1,
      },
      {
        productId: 1003,
        quantity: 1,
      },
    ],
    user: {
      id: 'fnu45dDwHq5v6Jv156bL1s5ev',
      name: 'User Test 1',
      email: 'testuser1@email.com',
    },
    status: 'APPROVED',
    createdAt: new Date(),
    updatedAt: new Date(),
  };
  await Order.create(firstOrder);

  const secondOrder: IOrder = {
    products: [
      {
        productId: 1001,
        quantity: 4,
      },
      {
        productId: 1003,
        quantity: 2,
      },
    ],
    user: {
      id: 'hrt89dWwHq5v6Mv158bL1H5bn',
      name: 'User Test 2',
      email: 'testuser2@email.com',
    },
    status: 'REJECTED',
    createdAt: new Date(),
    updatedAt: new Date(),
  };
  await Order.create(secondOrder);
}
