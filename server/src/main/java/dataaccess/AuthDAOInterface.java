package dataaccess;

import model.AuthData;

public interface AuthDAOInterface {

    void createAuth(AuthData a) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void deleteAuth(AuthData a) throws DataAccessException;

    void clearAuth() throws DataAccessException;
}
