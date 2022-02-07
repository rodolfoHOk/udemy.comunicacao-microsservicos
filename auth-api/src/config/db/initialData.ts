import bcrypt from 'bcrypt';
import User from '../../modules/user/model/User';

export async function createInitialData() {
  try {
    await User.sync({ force: true });

    let password = await bcrypt.hash('123456', 10);

    await User.create({
      name: 'User Test 1',
      email: 'testuser1@email.com',
      password: password,
    });

    await User.create({
      name: 'User Test 2',
      email: 'testuser2@email.com',
      password: password,
    });
  } catch (err) {
    console.error(err);
  }
}
