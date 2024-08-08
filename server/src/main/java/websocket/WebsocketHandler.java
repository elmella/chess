package websocket;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UnauthorizedException;
import model.GameData;
import service.AuthService;
import service.GameService;
import spark.Request;
import spark.Response;
import websocket.commands.UserGameCommand;

public class WebsocketHandler {

    public void authorize(UserGameCommand command) throws UnauthorizedException, DataAccessException {

        String authToken = command.getAuthToken();
        AuthService auth = new AuthService(AuthDAO.getInstance());

        if (!auth.authorize(authToken)) {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    public String getUsername(UserGameCommand command) throws DataAccessException {
        String authToken = command.getAuthToken();
        AuthService auth = new AuthService(AuthDAO.getInstance());

        return auth.getUsername(authToken);
    }

    public ChessGame.TeamColor getColor(UserGameCommand command) throws DataAccessException {
        String username = getUsername(command);
        int gameID = command.getGameID();

        GameService game = new GameService(GameDAO.getInstance());

        GameData gameData = game.getGame(gameID);

        if (username.equals(gameData.getWhiteUsername())) {
            return ChessGame.TeamColor.WHITE;
        } else if (username.equals(gameData.getBlackUsername())) {
            return ChessGame.TeamColor.BLACK;
        } else {
            return null;
        }
    }
}
