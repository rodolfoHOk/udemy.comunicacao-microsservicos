import { Request } from 'express';

import UserRepository from '../repository/UserRepository';
import * as httpStatus from '../../../config/constants/httpStatus';
import UserException from '../exception/UserException';
import { UserInstance } from '../model/User';

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

class UserService {
  async findByEmail(req: Request<Params>): Promise<UserResponse | Problem> {
    try {
      const { email } = req.params;
      this.validateEmail(email);
      let user = await UserRepository.findByEmail(email);
      this.validateUser(user);
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

  validateEmail(email: string): void {
    if (!email) {
      throw new UserException(
        httpStatus.BAD_REQUEST,
        'User email was not informed'
      );
    }
  }

  validateUser(user: UserInstance): void {
    if (!user) {
      throw new UserException(
        httpStatus.NOT_FOUND,
        'Not exist user with informed email'
      );
    }
  }
}

export default new UserService();
