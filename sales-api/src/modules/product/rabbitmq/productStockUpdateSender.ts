import amqp from 'amqplib';

import { RABBIT_MQ_URL } from '../../../config/constants/secrets';
import {
  PRODUCT_TOPIC,
  PRODUCT_STOCK_UPDATE_ROUTING_KEY,
} from '../../../config/rabbitmq/queue';

interface ProductQuantity {
  productId: number;
  quantity: number;
}

export interface ProductStockUpdateMessage {
  salesId: string;
  products: ProductQuantity[];
}

export function sendMessageToProductStockUpdateQueue(
  message: ProductStockUpdateMessage
) {
  amqp.connect(RABBIT_MQ_URL).then(
    (connection) => {
      connection.createChannel().then(
        (channel) => {
          let jsonStringMessage = JSON.stringify(message);
          console.info(
            `Sending message to product update stock: ${jsonStringMessage}`
          );
          channel.publish(
            PRODUCT_TOPIC,
            PRODUCT_STOCK_UPDATE_ROUTING_KEY,
            Buffer.from(jsonStringMessage)
          );
          console.info('Message was sent successfully');
        },
        (error) => {
          throw error;
        }
      );
    },
    (error) => {
      throw error;
    }
  );
}
