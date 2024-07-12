package handler;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.UnauthorizedException;
import result.ListGamesResponse;
import service.GameService;
import spark.Request;
import spark.Response;

public class ListGamesHandler extends Handler {

    public String handleRequest(Request req, Response res) {

        GameService game = new GameService(MemoryGameDAO.getInstance(), MemoryAuthDAO.getInstance());

        ListGamesResponse result = null;
        try {
            // Authorize
            authorize(req, res);
            result = game.getGames();
        } catch (DataAccessException e) {
            res.status(500);
            return UseGson.toJson(new result.Response(e.getMessage(), false));
        } catch (UnauthorizedException e) {
            res.status(401);
            return UseGson.toJson(new result.Response(e.getMessage(), false));
        }

        return UseGson.toJson(result);
    }
}
