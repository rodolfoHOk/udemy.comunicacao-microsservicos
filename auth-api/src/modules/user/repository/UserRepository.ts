import User, { UserInstance } from '../model/User';

class UserRepository {
  async findById(id: number): Promise<UserInstance> {
    try {
      return await User.findOne({ where: { id } });
    } catch (err) {
      console.error(err.message);
    }
  }

  async findByEmail(email: string): Promise<UserInstance> {
    try {
      return await User.findOne({ where: { email } });
    } catch (err) {
      console.error(err.message);
    }
  }
}

export default new UserRepository();
