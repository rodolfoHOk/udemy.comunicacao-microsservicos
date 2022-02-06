import { Sequelize } from 'sequelize';

const sequelize: Sequelize = new Sequelize('auth-db', 'postgres', 'postgres', {
  host: 'localhost',
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
