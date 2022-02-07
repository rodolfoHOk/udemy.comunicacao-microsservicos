import UserRepository from '../repository/UserRepository';
import * as httpStatus from '../../../config/constants/httpStatus';
import { Request } from 'express';

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

class UserService {
  async findByEmail(req: Request<User>): Promise<UserResponse | Problem> {
    try {
      const { email } = req.params;
      this.validateEmail(email);
      let user = await UserRepository.findByEmail(email);
      if (!user) {
        return {
          status: httpStatus.BAD_REQUEST,
          message: 'Not exist user with informed email',
        };
      }
      return {
        status: httpStatus.OK,
        user: {
          id: user.id,
          name: user.name,
          email: user.email,
        },
      };
    } catch (err) {
      return {
        status: err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR,
        message: err.message,
      };
    }
  }

  validateEmail(email: string) {
    if (!email) {
      throw new Error('User email was not informed');
    }
  }
}

export default new UserService();
