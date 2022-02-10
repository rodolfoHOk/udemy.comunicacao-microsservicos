import amqp from 'amqplib';

import { RABBIT_MQ_URL } from '../../../config/constants/secrets';
import { SALES_CONFIRMATION_QUEUE } from '../../../config/rabbitmq/queue';

export function listenToSalesConfirmationQueue() {
  amqp.connect(RABBIT_MQ_URL).then(
    (connection) => {
      console.info('Listening to sales confirmation queue...');
      connection.createChannel().then(
        (channel) => {
          channel.consume(
            SALES_CONFIRMATION_QUEUE,
            (message) =>
              console.info(
                `Receive message from queue: ${message.content.toString()}`
              ),
            { noAck: true }
          );
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
