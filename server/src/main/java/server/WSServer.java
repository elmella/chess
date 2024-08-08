package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import service.AuthService;
import websocket.LoadGame;
import websocket.commands.UserGameCommand;
import websocket.commands.*;
import websocket.messages.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WSServer {
    private final Gson commandSerializer = CommandSerializer.createSerializer();
    private final Gson messageSerializer = MessageSerializer.createSerializer();

    private final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Session>> gameUserSessionMap = new ConcurrentHashMap<>();

//    public static void main(String[] args) {
//        Spark.port(8080);
//        Spark.webSocket("/ws", WSServer.class);
//        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
//    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {
            UserGameCommand command = commandSerializer.fromJson(message, UserGameCommand.class);

            // Throws a custom UnauthorizedException. Yours may work differently.
            authorize(command);
            String username = getUsername(command);

            saveSession(command.getGameID(), username, session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, (ConnectCommand) command);
//                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
//                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
//                case RESIGN -> resign(session, username, (ResignCommand) command);
            }
        } catch (UnauthorizedException e) {
            // Serializes and sends the error message
            sendMessage(session.getRemote(), new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: unauthorized"));
        } catch (Exception e) {
            e.printStackTrace();
            sendMessage(session.getRemote(), new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + e.getMessage()));
        }
    }

    private void sendMessage(RemoteEndpoint remote, ServerMessage message) {
        try {
            String jsonResponse = MessageSerializer.createSerializer().toJson(message);
            System.out.println(jsonResponse);
            remote.sendString(jsonResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void authorize(UserGameCommand command) throws UnauthorizedException, DataAccessException {

        String authToken = command.getAuthToken();
        AuthService auth = new AuthService(AuthDAO.getInstance());

        if (!auth.authorize(authToken)) {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    private String getUsername(UserGameCommand command) throws UnauthorizedException, DataAccessException {
        AuthService auth = new AuthService(AuthDAO.getInstance());
        return auth.getUsername(command.getAuthToken());
    }

    private void saveSession(int gameID, String username, Session session) {
        gameUserSessionMap
                .computeIfAbsent(gameID, k -> new ConcurrentHashMap<>())
                .put(username, session);
    }

    private void connect(Session session, String username, ConnectCommand command) {

        // Load game for others
        LoadGameMessage loadGameMessage = (LoadGameMessage) new LoadGame().handleRequest(command);
        sendMessage(session.getRemote(), loadGameMessage);

        // Create notification
        String notification;
        String color = command.color();
        if (color == null) {
            notification = username + " joined as an observer";
        } else {
            notification = username + " joined as player " + color;
        }
        NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notification);

        ConcurrentHashMap<String, Session> userSessions = gameUserSessionMap.get(command.getGameID());
        for (Map.Entry<String, Session> entry : userSessions.entrySet()) {
            if (!entry.getKey().equals(username)) {
                sendMessage(entry.getValue().getRemote(), notificationMessage);
            }
        }



    }

}