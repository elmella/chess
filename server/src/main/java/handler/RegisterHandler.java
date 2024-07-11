package handler;

import dataaccess.AlreadyTakenException;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import request.RegisterRequest;
import result.LoginResponse;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler {

    public String handleRequest(Request req, Response res) {
        RegisterRequest request = (RegisterRequest) UseGson.fromJson(req.body(), RegisterRequest.class);

        if (hasNullFields(request)) {
            res.status(400);
            return UseGson.toJson(new result.Response("Error: bad request", false));
        }

        UserService user = new UserService(MemoryUserDAO.getInstance(), MemoryAuthDAO.getInstance());

        try {
            LoginResponse result = user.register(request);
            return UseGson.toJson(result);
        } catch (AlreadyTakenException e) {
            res.status(403);
            return UseGson.toJson(new result.Response(e.getMessage(), false));
        } catch (DataAccessException e) {
            res.status(500);
            return UseGson.toJson(new result.Response(e.getMessage(), false));
        }

    }
}
