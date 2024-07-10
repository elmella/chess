package handler;

import dataaccess.*;
import request.LoginRequest;
import result.LoginResponse;
import service.UserService;
import spark.*;


public class LoginHandler {

    public String handleRequest(Request req, Response res) throws DataAccessException {
        LoginRequest request = (LoginRequest) UseGson.fromJson(req.body(), LoginRequest.class);

        UserService user = new UserService(MemoryUserDAO.getInstance(), MemoryAuthDAO.getInstance());

        LoginResponse result = user.login(request);

        if (!result.isSuccess()) {
            res.status(401);
        }
        return UseGson.toJson(result);
    }
}
