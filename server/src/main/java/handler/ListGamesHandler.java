package handler;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import result.ListGamesResponse;
import service.GameService;
import spark.Request;
import spark.Response;

public class ListGamesHandler extends Handler {

    public String handleRequest(Request req, Response res) {

        if (!authorize(req, res)) {
            return UseGson.toJson(new result.Response("Error: unauthorized", false));
        }

        GameService game = new GameService(MemoryGameDAO.getInstance(), MemoryAuthDAO.getInstance());

        ListGamesResponse result = null;
        try {
            result = game.getGames();
        } catch (DataAccessException e) {
            res.status(500);
            return UseGson.toJson(new result.Response(e.getMessage(), false));
        }

        if (!result.isSuccess()) {
            res.status(500);
        }
        return UseGson.toJson(result);
    }
}
