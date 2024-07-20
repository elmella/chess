package handler;

import dataaccess.*;
import request.CreateGameRequest;
import request.CreateGameResponse;
import service.GameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler extends Handler {

    public String handleRequest(Request req, Response res) {

        CreateGameRequest request = (CreateGameRequest) UseGson.fromJson(req.body(), CreateGameRequest.class);

        GameService game = new GameService(GameDAO.getInstance(), AuthDAO.getInstance());

        CreateGameResponse result = null;
        try {
            // Authorize
            authorize(req, res);
            result = game.createGame(request);
        } catch (DataAccessException e) {
            res.status(500);
            return UseGson.toJson(new result.Response(e.getMessage(), false));
        } catch (BadRequestException e) {
            res.status(400);
            return UseGson.toJson(new result.Response(e.getMessage(), false));
        } catch (UnauthorizedException e) {
            res.status(401);
            return UseGson.toJson(new result.Response(e.getMessage(), false));
        }
        return UseGson.toJson(result);

    }

}
