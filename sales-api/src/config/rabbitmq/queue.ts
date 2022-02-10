const env = process.env;

export const PRODUCT_TOPIC = env.PRODUCT_TOPIC
  ? env.PRODUCT_TOPIC
  : 'product.topic';

export const PRODUCT_STOCK_UPDATE_ROUTING_KEY =
  env.PRODUCT_STOCK_UPDATE_ROUTING_KEY
    ? env.PRODUCT_STOCK_UPDATE_ROUTING_KEY
    : 'product-stock-update.routingKey';

export const SALES_CONFIRMATION_ROUTING_KEY = env.SALES_CONFIRMATION_ROUTING_KEY
  ? env.SALES_CONFIRMATION_ROUTING_KEY
  : 'sales-confirmation.routingKey';

export const PRODUCT_STOCK_UPDATE_QUEUE = env.PRODUCT_STOCK_UPDATE_QUEUE
  ? env.PRODUCT_STOCK_UPDATE_QUEUE
  : 'product-stock-update.queue';

export const SALES_CONFIRMATION_QUEUE = env.SALES_CONFIRMATION_QUEUE
  ? env.SALES_CONFIRMATION_QUEUE
  : 'sales-confirmation.queue';
