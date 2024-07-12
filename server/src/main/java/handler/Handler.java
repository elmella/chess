package handler;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.UnauthorizedException;
import service.AuthService;
import spark.Request;
import spark.Response;

import java.lang.reflect.Field;

public class Handler {

    public void authorize(Request req, Response res) throws UnauthorizedException, DataAccessException {

        String authToken = req.headers("authorization");
        AuthService auth = new AuthService(MemoryAuthDAO.getInstance());

        if (!auth.authorize(authToken)) {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }
}
