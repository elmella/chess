package handler;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import result.ListGamesResponse;
import service.ClearService;
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
        result = game.getGames();

        if (!result.isSuccess()) {
            res.status(500);
        }
        return UseGson.toJson(result);
    }
}
