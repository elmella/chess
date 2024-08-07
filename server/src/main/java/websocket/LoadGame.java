package websocket;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UnauthorizedException;
import handler.Handler;
import handler.UseGson;
import result.ListGamesResponse;
import service.GameService;
import spark.Request;
import spark.Response;
import websocket.commands.ConnectCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import javax.websocket.Session;

public class LoadGame extends WebsocketHandler {

    public ServerMessage handleRequest(UserGameCommand command) {

        GameService game = new GameService(GameDAO.getInstance());

        LoadGameMessage result = null;
        try {
            // Authorize
            authorize(command);

            // List games
            result = game.loadGame((ConnectCommand) command);
        } catch (DataAccessException e) {
            return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Data Access Exception, Error: " + e.getMessage());
        } catch (UnauthorizedException e) {
            return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: unauthorized");
        }

        return result;
    }
}
