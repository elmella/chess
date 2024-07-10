package handler;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import service.UserService;
import spark.Request;
import spark.Response;

public class LogoutHandler {

    public String handleRequest(Request req, Response res) throws DataAccessException {

        String authToken = req.headers("authorization");
        UserService user = new UserService(MemoryUserDAO.getInstance(), MemoryAuthDAO.getInstance());

        result.Response result = user.logout(authToken);

        if (!result.isSuccess()) {
            res.status(401);
        }
        return UseGson.toJson(result);
    }
}
