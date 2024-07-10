package service;

import dataaccess.AuthDAOInterface;
import dataaccess.DataAccessException;
import dataaccess.UserDAOInterface;
import model.AuthData;

public class AuthService {

    private final AuthDAOInterface authDAO;

    public AuthService(AuthDAOInterface authDAO) {
        this.authDAO = authDAO;
    }

    public boolean authorize(String authToken) {
        try {
            return authDAO.getAuth(authToken) != null;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
