import AuthController from '../controller/AuthController';
import { Router } from 'express';

const authRouter = Router();

authRouter.post('/', AuthController.getAccessToken);

export default authRouter;
