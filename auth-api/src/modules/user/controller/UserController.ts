import { Request, Response } from 'express';
import UserService from '../service/UserService';

interface Params {
  email: string;
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

class UserController {
  async findByEmail(
    req: Request<Params>,
    res: Response<UserResponse | Problem>
  ) {
    let user = await UserService.findByEmail(req);
    return res.status(user.status).json(user);
  }
}

export default new UserController();
