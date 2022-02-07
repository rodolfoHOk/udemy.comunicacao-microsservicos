import { Request } from 'express';

import UserRepository from '../repository/UserRepository';
import * as httpStatus from '../../../config/constants/httpStatus';
import UserException from '../exception/UserException';
import { UserInstance } from '../model/User';

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

class UserService {
  async findByEmail(req: AuthUserInfoRequest): Promise<UserResponse | Problem> {
    try {
      const { email } = req.params;
      this.validateEmail(email);

      let user = await UserRepository.findByEmail(email);
      this.validateUserByEmail(user);

      const { authUser } = req;
      this.validateAuthenticatedUser(user, authUser);

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

  async findById(req: AuthUserInfoRequest): Promise<UserResponse | Problem> {
    try {
      const { id } = req.params;
      this.validateId(id);

      const user = await UserRepository.findById(parseInt(id));
      this.validateUserById(user);

      const { authUser } = req;
      this.validateAuthenticatedUser(user, authUser);

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

  validateUserByEmail(user: UserInstance): void {
    if (!user) {
      throw new UserException(
        httpStatus.NOT_FOUND,
        'Not exist user with informed email'
      );
    }
  }

  validateAuthenticatedUser(user, authUser): void {
    if (!authUser || authUser.id !== user.id) {
      throw new UserException(
        httpStatus.FORBIDDEN,
        'You cannot see this user data'
      );
    }
  }

  validateId(id: string): void {
    if (!id) {
      throw new UserException(
        httpStatus.BAD_REQUEST,
        'User id was not informed'
      );
    }
    if (isNaN(parseInt(id))) {
      throw new UserException(
        httpStatus.BAD_REQUEST,
        'User id must be a number'
      );
    }
  }

  validateUserById(user: UserInstance): void {
    if (!user) {
      throw new UserException(
        httpStatus.NOT_FOUND,
        'Not exist user with informed id'
      );
    }
  }
}

export default new UserService();
