import { Sequelize } from 'sequelize';

import {
  DB_HOST,
  DB_PORT,
  DB_NAME,
  DB_USER,
  DB_PASSWORD,
} from '../constants/secrets';

const sequelize: Sequelize = new Sequelize(DB_NAME, DB_USER, DB_PASSWORD, {
  host: DB_HOST,
  port: Number(DB_PORT),
  dialect: 'postgres',
  quoteIdentifiers: false,
  define: {
    timestamps: false,
    underscored: true,
    freezeTableName: true,
  },
});

sequelize
  .authenticate()
  .then(() => console.info('Connection to database has been established'))
  .catch((err) => {
    console.error('Unable to connect to the database');
    console.error(err.message);
  });

export default sequelize;
