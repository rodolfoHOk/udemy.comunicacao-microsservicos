import AuthController from '../controller/AuthController';
import { Router } from 'express';

const authRouter = Router();

authRouter.post('/api/auth', AuthController.getAccessToken);

export default authRouter;
