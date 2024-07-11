package dataaccess;

import model.UserData;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAOInterface {
    private static MemoryUserDAO instance;
    ArrayList<UserData> user;


    public MemoryUserDAO() {
        user = new ArrayList<>();
    }

    public static MemoryUserDAO getInstance() {
        if (instance == null) {
            instance = new MemoryUserDAO();
        }
        return instance;
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
        return null;
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
