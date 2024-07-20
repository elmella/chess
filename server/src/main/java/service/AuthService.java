package service;

import dataaccess.AuthDAOInterface;
import dataaccess.DataAccessException;

public class AuthService {

    private final AuthDAOInterface authDAO;

    public AuthService(AuthDAOInterface authDAO) {
        this.authDAO = authDAO;
    }

    public boolean authorize(String authToken) throws DataAccessException {
        return authDAO.getAuth(authToken).authToken() != null;
    }
}
