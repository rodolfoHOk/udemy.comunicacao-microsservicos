import { NextFunction, Request, Response } from 'express';
import { v4 as uuidv4 } from 'uuid';
import { IncomingHttpHeaders } from 'http';

import { BAD_REQUEST } from './constants/httpStatus';

interface TracingRequest extends Request {
  headers: IncomingHttpHeaders & {
    transactionid?: string;
    serviceid?: string;
  };
}

interface Problem {
  status: number;
  message: string;
}

export default (
  req: TracingRequest,
  res: Response<Problem>,
  next: NextFunction
) => {
  let { transactionid } = req.headers;
  if (!transactionid) {
    return res.status(BAD_REQUEST).json({
      status: BAD_REQUEST,
      message: 'The transactionid header is required',
    });
  }
  req.headers.serviceid = uuidv4();
  return next();
};
