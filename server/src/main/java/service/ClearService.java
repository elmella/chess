package service;

import dataaccess.AuthDAOInterface;
import dataaccess.DataAccessException;
import dataaccess.GameDAOInterface;
import dataaccess.UserDAOInterface;
import result.Response;

public class ClearService {
    private final AuthDAOInterface authDAO;
    private final GameDAOInterface gameDAO;
    private final UserDAOInterface userDAO;


    public ClearService(AuthDAOInterface authDAO, GameDAOInterface gameDAO, UserDAOInterface userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public Response clear() throws DataAccessException {
        authDAO.clearAuth();
        gameDAO.clearGame();
        userDAO.clearUser();
        return new Response(null, true);
    }
}
