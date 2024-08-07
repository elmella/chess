package handler;

import dataaccess.*;
import service.AuthService;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler {

    public String handleRequest(Request req, Response res) {

        String authToken = req.headers("authorization");
        AuthService auth = new AuthService(AuthDAO.getInstance());

        try {
            result.Response result = auth.logout(authToken);
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
