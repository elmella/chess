package handler;

import dataaccess.*;
import request.RegisterRequest;
import result.LoginResponse;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler {

    public String handleRequest(Request req, Response res) {
        RegisterRequest request = (RegisterRequest) UseGson.fromJson(req.body(), RegisterRequest.class);

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
        } catch (UnauthorizedException e) {
            res.status(401);
            return UseGson.toJson(new result.Response(e.getMessage(), false));
        } catch (BadRequestException e) {
            res.status(400);
            return UseGson.toJson(new result.Response(e.getMessage(), false));
        }
    }
}
