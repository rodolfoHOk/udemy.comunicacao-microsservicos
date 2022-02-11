import { Request } from 'express';
import { IncomingHttpHeaders } from 'http';
import bcrypt from 'bcrypt';
import jwt from 'jsonwebtoken';

import UserException from '../../user/exception/UserException';
import * as httpStatus from '../../../config/constants/httpStatus';
import UserRepository from '../../user/repository/UserRepository';
import UserService from '../../user/service/UserService';
import { API_SECRET } from '../../../config/constants/secrets';

interface AuthRequestBody {
  email: string;
  password: string;
}

interface AuthRequest extends Request<AuthRequestBody> {
  headers: IncomingHttpHeaders & {
    transactionid?: string;
    serviceid?: string;
  };
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
  async getAccessToken(req: AuthRequest): Promise<AuthResponse | Problem> {
    try {
      const { transactionid, serviceid } = req.headers;
      let reqBodyWithoutPass: AuthRequestBody = JSON.parse(
        JSON.stringify(req.body)
      );
      reqBodyWithoutPass.password = '??????????';
      console.info(
        `Request to POST /auth with data ${JSON.stringify(
          reqBodyWithoutPass
        )} | [transactionid: ${transactionid} | serviceid: ${serviceid}]`
      );

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
      let response: AuthResponse = {
        status: httpStatus.OK,
        accessToken,
      };

      console.info(
        `Response to POST /auth with data ${JSON.stringify(
          response
        )} | [transactionid: ${transactionid} | serviceid: ${serviceid}]`
      );
      return response;
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
