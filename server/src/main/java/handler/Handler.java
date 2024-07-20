package handler;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import service.AuthService;
import spark.Request;
import spark.Response;

public class Handler {

    public void authorize(Request req, Response res) throws UnauthorizedException, DataAccessException {

        String authToken = req.headers("authorization");
        AuthService auth = new AuthService(AuthDAO.getInstance());

        if (!auth.authorize(authToken)) {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }
}
