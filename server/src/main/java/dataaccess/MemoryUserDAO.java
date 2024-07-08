package dataaccess;

import model.UserData;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAOInterface {
    ArrayList<UserData> user;

    public MemoryUserDAO() {
        user = new ArrayList<>();
    }

    @Override
    public void createUser(UserData u) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username, String password) throws DataAccessException {
        return null;
    }

    @Override
    public void updateUser(UserData u) throws DataAccessException {

    }

    @Override
    public void deleteUser(UserData u) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
