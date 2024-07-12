package handler;

import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import request.CreateGameRequest;
import request.CreateGameResponse;
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

        // Verify valid request body
//        if (hasNullFields(request)) {
//            res.status(400);
//            return UseGson.toJson(new result.Response("Error: bad request", false));
//        }

        GameService game = new GameService(MemoryGameDAO.getInstance(), MemoryAuthDAO.getInstance());

        CreateGameResponse result = null;
        try {
            result = game.createGame(request);
        } catch (DataAccessException e) {
            res.status(500);
            return UseGson.toJson(new result.Response(e.getMessage(), false));
        } catch (BadRequestException e) {
            res.status(400);
            return UseGson.toJson(new result.Response(e.getMessage(), false));
        }
        return UseGson.toJson(result);

    }

}
