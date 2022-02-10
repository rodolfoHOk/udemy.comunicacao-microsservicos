import { NextFunction, Request, Response } from 'express';
import jwt from 'jsonwebtoken';

import { UNAUTHORIZED, INTERNAL_SERVER_ERROR } from '../constants/httpStatus';
import { API_SECRET } from '../constants/secrets';
import AuthException from './AuthException';

const bearer = 'Bearer ';

interface AuthUser {
  id: number;
  name: string;
  email: string;
}

interface TokenJWT {
  authUser: AuthUser;
}

interface AuthUserInfoRequest extends Request {
  authUser: AuthUser;
}

interface Problem {
  status: number;
  message: string;
}

export async function checkToken(
  req: AuthUserInfoRequest,
  res: Response<Problem>,
  next: NextFunction
) {
  try {
    const { authorization } = req.headers;
    if (!authorization) {
      throw new AuthException(UNAUTHORIZED, 'Access token was not informed');
    }

    let accessToken = authorization;
    if (accessToken.includes(bearer)) {
      accessToken = accessToken.split(' ')[1];
    }
    const decoded = (await jwt.verify(accessToken, API_SECRET)) as TokenJWT;
    req.authUser = decoded.authUser;

    return next();
  } catch (err) {
    const status = err.status ? err.status : INTERNAL_SERVER_ERROR;
    return res.status(status).json({
      status,
      message: err.message,
    });
  }
}
