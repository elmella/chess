package websocket;

import dataaccess.*;
import service.GameService;
import websocket.commands.ResignCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
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


            // Resign
            game.resign(resignCommand);

            notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, " has resigned");

        } catch (DataAccessException e) {
            return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Data Access Exception, Error: " + e.getMessage());
        } catch (NotPlayerException | UnauthorizedException | GameOverException e) {
            return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, e.getMessage());
        }

        return notificationMessage;
    }
}