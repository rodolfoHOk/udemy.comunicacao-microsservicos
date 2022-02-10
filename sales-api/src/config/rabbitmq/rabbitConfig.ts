import amqp, { Channel, Connection } from 'amqplib';

import {
  PRODUCT_TOPIC,
  PRODUCT_STOCK_UPDATE_ROUTING_KEY,
  SALES_CONFIRMATION_ROUTING_KEY,
  PRODUCT_STOCK_UPDATE_QUEUE,
  SALES_CONFIRMATION_QUEUE,
} from './queue';

import { RABBIT_MQ_URL } from '../constants/secrets';

const HALF_SECONDS = 500;

export async function connectRabbitMq() {
  amqp
    .connect(RABBIT_MQ_URL)
    .then((connection) => {
      createQueue(
        connection,
        PRODUCT_STOCK_UPDATE_QUEUE,
        PRODUCT_STOCK_UPDATE_ROUTING_KEY,
        PRODUCT_TOPIC
      );
      createQueue(
        connection,
        SALES_CONFIRMATION_QUEUE,
        SALES_CONFIRMATION_ROUTING_KEY,
        PRODUCT_TOPIC
      );
      setTimeout(() => connection.close(), HALF_SECONDS);
    })
    .catch((error) => {
      throw new Error(error);
    });

  function createQueue(
    connection: Connection,
    queue: string,
    routingKey: string,
    topic: string
  ) {
    connection
      .createChannel()
      .then((channel) => {
        channel.assertExchange(topic, 'topic', { durable: true });
        channel.assertQueue(queue, { durable: true });
        channel.bindQueue(queue, topic, routingKey);
      })
      .catch((error) => {
        throw new Error(error);
      });
  }
}
