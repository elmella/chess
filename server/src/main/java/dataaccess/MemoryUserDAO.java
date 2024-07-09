package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAOInterface {
    ArrayList<UserData> user;

    public MemoryUserDAO() {
        user = new ArrayList<>();
    }

    @Override
    public void createUser(UserData u) throws DataAccessException {
        user.add(u);
    }

    @Override
    public UserData getUser(String username, String password) throws DataAccessException {
        for (UserData u : user) {
            if (u.username().equals(username) && u.password().equals(password)) {
                return u;
            }
        }
        throw new DataAccessException("Username or Password not found");
    }

    @Override
    public void updateUser(UserData u) throws DataAccessException {
        if (!user.contains(u)) {
            throw new DataAccessException("User not found");
        }
        user.remove(u);
        user.add(u);
    }

    @Override
    public void deleteUser(UserData u) throws DataAccessException {
        if (!user.contains(u)) {
            throw new DataAccessException("User not found");
        }
        user.remove(u);
    }

    @Override
    public void clearUser() throws DataAccessException {
        user = new ArrayList<>();
    }
}
