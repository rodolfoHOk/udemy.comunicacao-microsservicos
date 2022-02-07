import express, { Express } from 'express';
import * as db from './config/db/initialData';
import userRouter from './modules/user/router/UserRouter';
import authRouter from './modules/auth/router/AuthRouter';

const env = process.env;
const PORT = env.PORT || 8080;

const app: Express = express();

db.createInitialData();

app.use(express.json());

app.use(userRouter);
app.use(authRouter);

app.get('/api/status', (req, res) => {
  return res.status(200).json({
    service: 'Auth-API',
    status: 'up',
    httpStatus: 200,
  });
});

app.listen(PORT, () => {
  console.info(`Server started successfully at port ${PORT}`);
});
