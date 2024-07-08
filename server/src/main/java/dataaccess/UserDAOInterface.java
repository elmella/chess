package dataaccess;

import model.UserData;

public interface UserDAOInterface {
    void createUser(UserData u) throws DataAccessException;

    UserData getUser(String username, String password) throws DataAccessException;

    void updateUser(UserData u) throws DataAccessException;

    void deleteUser(UserData u) throws DataAccessException;

    void clear() throws DataAccessException;
}