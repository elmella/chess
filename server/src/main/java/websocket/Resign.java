package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.GameOverException;
import dataaccess.UnauthorizedException;
import service.GameService;
import websocket.commands.LeaveGameCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.ResignCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public class Resign extends WebsocketHandler {

    public ServerMessage handleRequest(UserGameCommand command) {

        GameService game = new GameService(GameDAO.getInstance());

        ResignCommand resignCommand = (ResignCommand) command;

        NotificationMessage notificationMessage = null;
        try {
            // Authorize
            authorize(command);

            // Get client color
            ChessGame.TeamColor clientColor = getColor(command);

            if (clientColor == null) {
                return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: must be player to resign");
            }
            // Resign
            game.resign(resignCommand);

            notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    " has resigned");

        } catch (DataAccessException e) {
            return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Data Access Exception, Error: " + e.getMessage());
        } catch (UnauthorizedException | GameOverException e) {
            return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage());
        }

        return notificationMessage;
    }
}