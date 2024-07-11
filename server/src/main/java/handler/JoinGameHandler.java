package handler;

import dataaccess.*;
import request.JoinGameRequest;
import service.GameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler {

    public String handleRequest(Request req, Response res) {
        String authToken = req.headers("authorization");

        // Authorize request
        if (!authorize(req, res)) {
            res.status(401);
            return UseGson.toJson(new result.Response("Error: unauthorized", false));
        }

        JoinGameRequest request = (JoinGameRequest) UseGson.fromJson(req.body(), JoinGameRequest.class);

        // Verify valid request body
        if (hasNullFields(request)) {
            res.status(400);
            return UseGson.toJson(new result.Response("Error: bad request", false));
        }

        GameService gameService = new GameService(MemoryGameDAO.getInstance(), MemoryAuthDAO.getInstance());

        try {
            result.Response result = gameService.joinGame(request, authToken);
            return UseGson.toJson(result);
        } catch (AlreadyTakenException e) {
            res.status(403);
            return UseGson.toJson(new result.Response(e.getMessage(), false));
        } catch (BadRequestException e) {
            res.status(400);
            return UseGson.toJson(new result.Response("Error: bad request", false));
        } catch (DataAccessException e) {
            res.status(500);
            return UseGson.toJson(new result.Response(e.getMessage(), false));
        }
    }
}
