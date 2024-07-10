package handler;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import request.RegisterRequest;
import result.LoginResponse;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler {

    public String handleRequest(Request req, Response res) throws DataAccessException {
        RegisterRequest request = (RegisterRequest) UseGson.fromJson(req.body(), RegisterRequest.class);

        if (hasNullFields(request)) {
            res.status(400);
            return UseGson.toJson(new result.Response("Error: bad request", false));
        }

        UserService user = new UserService(MemoryUserDAO.getInstance(), MemoryAuthDAO.getInstance());

        LoginResponse result = user.register(request);

        if (!result.isSuccess()) {
            res.status(403);
        }

        return UseGson.toJson(result);
    }
}