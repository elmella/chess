package handler;

import dataaccess.*;
import request.LoginRequest;
import result.LoginResponse;
import service.UserService;
import spark.Request;
import spark.Response;


public class LoginHandler {

    public String handleRequest(Request req, Response res) {
        LoginRequest request = (LoginRequest) UseGson.fromJson(req.body(), LoginRequest.class);

        UserService user = new UserService(UserDAO.getInstance(), AuthDAO.getInstance());

        try {
            LoginResponse result = user.login(request);
            return UseGson.toJson(result);
        } catch (UnauthorizedException e) {
            res.status(401);
            return UseGson.toJson(new result.Response(e.getMessage(), false));
        } catch (DataAccessException e) {
            res.status(500);
            return UseGson.toJson(new result.Response(e.getMessage(), false));
        }
    }
}
