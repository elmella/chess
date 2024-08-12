package server;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UnauthorizedException;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.AuthService;
import service.GameService;
import websocket.LeaveGame;
import websocket.LoadGame;
import websocket.MakeMove;
import websocket.Resign;
import websocket.commands.*;
import websocket.messages.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WSServer {

    private final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Session>> gameUserSessionMap = new ConcurrentHashMap<>();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {
            UserGameCommand command = CommandSerializer.createSerializer().fromJson(message, UserGameCommand.class);

            // Throws a custom UnauthorizedException. Yours may work differently.
            String username = getUsername(command);

            saveSession(command.getGameID(), username, session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, (ConnectCommand) command);
                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
                case RESIGN -> resign(session, username, (ResignCommand) command);
            }
        } catch (UnauthorizedException e) {
            // Serializes and sends the error message
            sendMessage(session.getRemote(), new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: unauthorized"));
        } catch (Exception e) {
            sendMessage(session.getRemote(), new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + e.getMessage()));
        }
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("WebSocket connection established: " + session.getRemoteAddress().getAddress());
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        System.out.println("WebSocket connection closed: " + reason);
    }

    private void sendMessage(RemoteEndpoint remote, ServerMessage message) {
        if (message != null) {
            try {
                // Serialize the message
                String jsonResponse = MessageSerializer.createSerializer().toJson(message);

                // Send to session
                remote.sendString(jsonResponse);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String getUsername(UserGameCommand command) throws UnauthorizedException, DataAccessException {
        AuthService auth = new AuthService(AuthDAO.getInstance());
        return auth.getUsername(command.getAuthToken());
    }

    private ChessGame.TeamColor getTeamColor(int gameID, String username) throws DataAccessException {
        GameService game = new GameService(GameDAO.getInstance());
        return game.getTeamColor(gameID, username);
    }

    private void saveSession(int gameID, String username, Session session) {
        gameUserSessionMap.computeIfAbsent(gameID, k -> new ConcurrentHashMap<>()).put(username, session);
    }

    private void connect(Session session, String username, ConnectCommand command) {

        // Load game for others
        ServerMessage serverMessage = new LoadGame().handleRequest(command);
        if (serverMessage.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)) {
            sendMessage(session.getRemote(), serverMessage);
            return;
        }

        // Cast if no error
        LoadGameMessage loadGameMessage = (LoadGameMessage) serverMessage;
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

    private void makeMove(Session session, String username, MakeMoveCommand command) {

        // Make move, then load game for everyone
        ServerMessage serverMessage = new MakeMove().handleRequest(command);
        if (serverMessage.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)) {
            sendMessage(session.getRemote(), serverMessage);
            return;
        }

        // Load game if no error
        LoadGameMessage loadGameMessage = (LoadGameMessage) serverMessage;

        // Get game sessions
        int gameId = command.getGameID();
        ConcurrentHashMap<String, Session> userSessions = gameUserSessionMap.get(gameId);

        // Create notifications
        NotificationMessage move = createNotification(loadGameMessage.getMoveNotification());
        NotificationMessage check = createNotification(loadGameMessage.getCheckNotification());
        NotificationMessage checkmate = createNotification(loadGameMessage.getCheckmateNotification());
        NotificationMessage stalemate = createNotification(loadGameMessage.getStalemateNotification());


        for (Map.Entry<String, Session> entry : userSessions.entrySet()) {
            RemoteEndpoint remote = entry.getValue().getRemote();

            String bystanderUsername = entry.getKey();
            ChessGame.TeamColor bystanderColor = null;
            try {
                 bystanderColor = getTeamColor(gameId, bystanderUsername);
            } catch (DataAccessException ignored) {
            }

            // Send load game to everyone
            loadGameMessage.setColor(bystanderColor);
            sendMessage(entry.getValue().getRemote(), loadGameMessage);

            // Send move to everyone else
            if (!entry.getKey().equals(username)) {
                sendMessage(remote, move);
            }

            // Send statuses (will not send if null)
            sendMessage(remote, check);
            sendMessage(remote, checkmate);
            sendMessage(remote, stalemate);
        }
    }

    private void leaveGame(Session session, String username, LeaveGameCommand command) {

        // Get all game clients
        ConcurrentHashMap<String, Session> userSessions = gameUserSessionMap.get(command.getGameID());

        ServerMessage serverMessage = new LeaveGame().handleRequest(command);
        if (serverMessage.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)) {
            sendMessage(session.getRemote(), serverMessage);
            return;
        }

        // Close the session
        session.close();

        // Leave the game if no error
        NotificationMessage leaveNotification = (NotificationMessage) serverMessage;

        gameUserSessionMap.remove(command.getGameID());

        userSessions.remove(username, session);

        // Add username to notification
        leaveNotification.setMessage(username + leaveNotification.getMessage());

        for (Map.Entry<String, Session> entry : userSessions.entrySet()) {
                sendMessage(entry.getValue().getRemote(), leaveNotification);
        }

        gameUserSessionMap.put(command.getGameID(), userSessions);
    }

    private void resign(Session session, String username, ResignCommand command) {

        // Get all game clients
        ConcurrentHashMap<String, Session> userSessions = gameUserSessionMap.get(command.getGameID());

        ServerMessage serverMessage = new Resign().handleRequest(command);
        if (serverMessage.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)) {
            sendMessage(session.getRemote(), serverMessage);
            return;
        }

        // Resign if no error
        NotificationMessage resignNotification = (NotificationMessage) serverMessage;

        // Add username to notification
        resignNotification.setMessage(username + resignNotification.getMessage());

        for (Map.Entry<String, Session> entry : userSessions.entrySet()) {
            sendMessage(entry.getValue().getRemote(), resignNotification);
        }
    }

    private NotificationMessage createNotification(String message) {
        if (message != null) {
            return new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        }
        return null;
    }

}