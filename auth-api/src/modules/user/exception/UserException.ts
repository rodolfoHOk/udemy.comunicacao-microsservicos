class UserException extends Error {
  status: number;

  constructor(status, message) {
    super(message);
    this.status = status;
    this.message = message;
    this.name = this.constructor.name;
    Error.captureStackTrace(this, this.constructor);
  }
}

export default UserException;
