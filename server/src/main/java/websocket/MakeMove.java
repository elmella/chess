package websocket;

import chess.InvalidMoveException;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.GameOverException;
import dataaccess.UnauthorizedException;
import service.GameService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

public class MakeMove extends WebsocketHandler {
    public ServerMessage handleRequest(UserGameCommand command) {

        GameService game = new GameService(GameDAO.getInstance());

        MakeMoveCommand makeMoveCommand = (MakeMoveCommand) command;

        LoadGameMessage result = null;
        try {
            // Authorize
            authorize(command);

            // Load the game
            result = game.makeMove(makeMoveCommand);

            if (result.getGame() == null) {
                return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Game not found");
            }
        } catch (DataAccessException e) {
            return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Data Access Exception, Error: " + e.getMessage());
        } catch (UnauthorizedException | InvalidMoveException | GameOverException e) {
            return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage());
        }

        return result;
    }
}
