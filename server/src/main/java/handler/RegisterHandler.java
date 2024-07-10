package handler;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import request.RegisterRequest;
import result.LoginResponse;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegisterHandler {

    public String handleRequest(Request req, Response res) throws DataAccessException {
        RegisterRequest request = (RegisterRequest) UseGson.fromJson(req.body(), RegisterRequest.class);

        UserService user = new UserService(MemoryUserDAO.getInstance(), MemoryAuthDAO.getInstance());

        LoginResponse result = user.register(request);

        System.out.println(UseGson.toJson(result));


        return UseGson.toJson(result);
    }
}
