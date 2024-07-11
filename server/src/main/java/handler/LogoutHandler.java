package handler;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import service.UserService;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler {

    public String handleRequest(Request req, Response res) {
        if (!authorize(req, res)) {
            return UseGson.toJson(new result.Response("Error: unauthorized", false));
        }

        String authToken = req.headers("authorization");
        UserService user = new UserService(MemoryUserDAO.getInstance(), MemoryAuthDAO.getInstance());

        try {
            result.Response result = user.logout(authToken);
            return UseGson.toJson(result);
        } catch (DataAccessException e) {
            res.status(500);
            return UseGson.toJson(new result.Response(e.getMessage(), false));
        }


    }
}
