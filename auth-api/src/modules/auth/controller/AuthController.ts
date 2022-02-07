import { Request, Response } from 'express';
import AuthService from '../service/AuthService';

interface AuthRequest {
  email: string;
  password: string;
}

interface Problem {
  status: number;
  message: string;
}

interface AuthResponse {
  status: number;
  accessToken: string;
}

class AuthController {
  async getAccessToken(
    req: Request<AuthRequest>,
    res: Response<AuthResponse | Problem>
  ) {
    let authResponse = await AuthService.getAccessToken(req);
    res.status(authResponse.status).json(authResponse);
  }
}

export default new AuthController();
