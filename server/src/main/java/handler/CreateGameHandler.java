package handler;

import dataaccess.MemoryGameDAO;
import request.CreateGameRequest;
import request.CreateGameResponse;
import request.LoginRequest;
import service.GameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler extends Handler {

    public String handleRequest(Request req, Response res) {

        // Authorize request
        if (!authorize(req, res)) {
            return UseGson.toJson(new result.Response("Error: unauthorized", false));
        }

        CreateGameRequest request = (CreateGameRequest) UseGson.fromJson(req.body(), CreateGameRequest.class);

        // Verify request is not bad
        if (hasNullFields(request)) {
            res.status(400);
            return UseGson.toJson(new result.Response("Error: bad request", false));
        }

        GameService game = new GameService(MemoryGameDAO.getInstance());

        CreateGameResponse result = game.createGame(request);

        return UseGson.toJson(result);

    }
}