package service;

import dataaccess.AuthDAOInterface;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import model.AuthData;
import result.Response;

public class AuthService {

    private final AuthDAOInterface authDAO;

    public AuthService(AuthDAOInterface authDAO) {
        this.authDAO = authDAO;
    }

    public boolean authorize(String authToken) throws DataAccessException {
        return authDAO.getAuth(authToken).authToken() != null;
    }

    public String getUsername(String authToken) throws DataAccessException {
        AuthData auth = authDAO.getAuth(authToken);
        return auth.username();
    }

    public Response logout(String authToken) throws DataAccessException, UnauthorizedException {
        AuthData auth = authDAO.getAuth(authToken);
        if (auth.authToken() == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        authDAO.deleteAuth(auth);
        return new Response(null, true);
    }
}
