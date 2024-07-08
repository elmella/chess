package dataaccess;

import model.AuthData;

public interface AuthDAOInterface {
    void createAuth(AuthData a) throws DataAccessException;

    AuthData getAuth(String username) throws DataAccessException;

    void deleteAuth(AuthData a) throws DataAccessException;

    void clear() throws DataAccessException;
}