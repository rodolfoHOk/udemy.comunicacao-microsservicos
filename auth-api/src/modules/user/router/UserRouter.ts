import { Router } from 'express';
import UserController from '../controller/UserController';

const userRouter: Router = Router();

userRouter.get('/api/user/email/:email', UserController.findByEmail);

export default userRouter;
