package handler;

import dataaccess.DataAccessException;
import request.LoginRequest;
import result.LoginResponse;
import service.UserService;
import spark.*;


public class LoginHandler {

    public String handleRequest(Request req, Response res) throws DataAccessException {
        LoginRequest request = (LoginRequest) UseGson.fromJson(req, LoginRequest.class);

        UserService user = new UserService();

        LoginResponse result = user.login(request);

        System.out.println(UseGson.toJson(result));

        return UseGson.toJson(result);
    }
}
