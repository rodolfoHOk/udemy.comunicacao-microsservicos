import mongoose from 'mongoose';

import { MONGO_DB_URL } from '../secrets/secrets';

export function connect() {
  mongoose.connect(MONGO_DB_URL);
  mongoose.connection.on('connected', () =>
    console.info('The application connected to MongoDB successfully')
  );
  mongoose.connection.on('error', () =>
    console.error('The application not connected to MongoDB')
  );
}
