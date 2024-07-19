package handler;

import dataaccess.*;
import service.UserService;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler {

    public String handleRequest(Request req, Response res) {

        String authToken = req.headers("authorization");
        UserService user = new UserService(UserDAO.getInstance(), AuthDAO.getInstance());

        try {
            result.Response result = user.logout(authToken);
            return UseGson.toJson(result);
        } catch (DataAccessException e) {
            res.status(500);
            return UseGson.toJson(new result.Response(e.getMessage(), false));
        } catch (UnauthorizedException e) {
            res.status(401);
            return UseGson.toJson(new result.Response(e.getMessage(), false));
        }


    }
}
