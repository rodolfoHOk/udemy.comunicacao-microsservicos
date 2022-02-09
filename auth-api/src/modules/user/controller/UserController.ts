import { Request, Response } from 'express';
import UserService from '../service/UserService';

interface Params {
  email?: string;
  id?: string;
}

interface User {
  id?: number;
  name: string;
  email: string;
  password?: string;
}

interface UserResponse {
  status: number;
  user: User;
}

interface Problem {
  status: number;
  message: string;
}

interface AuthUserInfoRequest extends Request<Params> {
  authUser: User;
}

class UserController {
  async findByEmail(
    req: AuthUserInfoRequest,
    res: Response<UserResponse | Problem>
  ) {
    let result = await UserService.findByEmail(req);
    return res.status(result.status).json(result);
  }

  async findById(
    req: AuthUserInfoRequest,
    res: Response<UserResponse | Problem>
  ) {
    const result = await UserService.findById(req);
    return res.status(result.status).json(result);
  }
}

export default new UserController();
