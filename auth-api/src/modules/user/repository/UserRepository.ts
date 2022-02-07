import User from '../model/User';

class UserRepository {
  async findById(id: number) {
    try {
      return await User.findOne({ where: { id } });
    } catch (err) {
      console.error(err.message);
    }
  }

  async findByEmail(email: string) {
    try {
      return await User.findOne({ where: { email } });
    } catch (err) {
      console.error(err.message);
    }
  }
}

export default new UserRepository();
