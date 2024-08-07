package handler;

import dataaccess.*;
import result.ListGamesResponse;
import service.GameService;
import spark.Request;
import spark.Response;

public class ListGamesHandler extends Handler {

    public String handleRequest(Request req, Response res) {

        GameService game = new GameService(GameDAO.getInstance());

        ListGamesResponse result = null;
        try {
            // Authorize
            authorize(req);

            // List games
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
