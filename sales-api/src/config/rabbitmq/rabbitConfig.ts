import amqp, { Connection } from 'amqplib';

import { RABBIT_MQ_URL } from '../constants/secrets';
import {
  PRODUCT_TOPIC,
  PRODUCT_STOCK_UPDATE_ROUTING_KEY,
  SALES_CONFIRMATION_ROUTING_KEY,
  PRODUCT_STOCK_UPDATE_QUEUE,
  SALES_CONFIRMATION_QUEUE,
} from './queue';
import { listenToSalesConfirmationQueue } from '../../modules/sales/rabbitmq/salesConfirmListener';

const TWO_SECONDS = 2000;
const HALF_MINUTE = 30000;
const CONTAINER_ENV = 'container';

export async function connectRabbitMq() {
  const env = process.env.NODE_ENV;
  if (CONTAINER_ENV === env) {
    console.info('Waiting for RabbitMQ to start...');
    setTimeout(async () => await connectRabbitMqAndCreateQueues(), HALF_MINUTE);
  } else {
    await connectRabbitMqAndCreateQueues();
  }

  async function connectRabbitMqAndCreateQueues() {
    amqp.connect(RABBIT_MQ_URL).then(
      (connection) => {
        console.info('Starting RabbitMQ...');
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
        console.info('Queues and Topics were defined');
        setTimeout(() => connection.close(), TWO_SECONDS);
      },
      (error) => {
        throw error;
      }
    );

    setTimeout(() => listenToSalesConfirmationQueue(), TWO_SECONDS);
  }

  function createQueue(
    connection: Connection,
    queue: string,
    routingKey: string,
    topic: string
  ) {
    connection.createChannel().then(
      (channel) => {
        channel.assertExchange(topic, 'topic', { durable: true });
        channel.assertQueue(queue, { durable: true });
        channel.bindQueue(queue, topic, routingKey);
      },
      (error) => {
        throw error;
      }
    );
  }
}
