import { Request } from 'express';
import bcrypt from 'bcrypt';
import jwt from 'jsonwebtoken';

import UserException from '../../user/exception/UserException';
import * as httpStatus from '../../../config/constants/httpStatus';
import UserRepository from '../../user/repository/UserRepository';
import UserService from '../../user/service/UserService';
import { API_SECRET } from '../../../config/constants/secrets';

interface AuthRequest {
  email: string;
  password: string;
}

interface Problem {
  status: number;
  message: string;
}

interface AuthUser {
  id: number;
  name: string;
  email: string;
}

interface AuthResponse {
  status: number;
  accessToken: string;
}

class AuthService {
  async getAccessToken(
    req: Request<AuthRequest>
  ): Promise<AuthResponse | Problem> {
    try {
      const { email, password } = req.body;
      this.validateAccessData(email, password);

      let user = await UserRepository.findByEmail(email);
      UserService.validateUserByEmail(user);
      await this.validatePassword(password, user.password);

      const authUser: AuthUser = {
        id: user.id,
        name: user.name,
        email: user.email,
      };
      const accessToken = jwt.sign({ authUser }, API_SECRET, {
        expiresIn: '1d',
      });

      return {
        status: httpStatus.OK,
        accessToken,
      };
    } catch (err) {
      return {
        status: err.status ? err.status : httpStatus.INTERNAL_SERVER_ERROR,
        message: err.message,
      };
    }
  }

  validateAccessData(email: string, password: string) {
    if (!email || !password) {
      throw new UserException(
        httpStatus.UNAUTHORIZED,
        'Email and password must be informed'
      );
    }
  }

  async validatePassword(password: string, hashPassword: string) {
    if (!(await bcrypt.compare(password, hashPassword))) {
      throw new UserException(
        httpStatus.UNAUTHORIZED,
        'Password does not match'
      );
    }
  }
}

export default new AuthService();
