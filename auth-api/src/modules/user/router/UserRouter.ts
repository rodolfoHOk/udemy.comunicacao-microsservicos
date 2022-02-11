import { Router } from 'express';
import UserController from '../controller/UserController';
import checkToken from '../../../config/auth/checkToken';

const userRouter: Router = Router();

userRouter.use(checkToken);

userRouter.get('/email/:email', UserController.findByEmail);
userRouter.get('/:id', UserController.findById);

export default userRouter;
