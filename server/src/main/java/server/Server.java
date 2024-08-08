package server;

import dataaccess.*;
import handler.*;
import spark.*;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

import java.util.HashMap;

public class Server {

    private final LoginHandler loginHandler;
    private final RegisterHandler registerHandler;
    private final ClearHandler clearHandler;
    private final LogoutHandler logoutHandler;
    private final ListGamesHandler listGamesHandler;
    private final CreateGameHandler createGameHandler;
    private final JoinGameHandler joinGameHandler;
    private final WSServer wsServer;

    public Server() {
        this.loginHandler = new LoginHandler();
        this.registerHandler = new RegisterHandler();
        this.clearHandler = new ClearHandler();
        this.logoutHandler = new LogoutHandler();
        this.listGamesHandler = new ListGamesHandler();
        this.createGameHandler = new CreateGameHandler();
        this.joinGameHandler = new JoinGameHandler();
        this.wsServer = new WSServer();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        try {
            DatabaseManager.createDatabase();
            DatabaseManager.createTables();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        Spark.staticFiles.location("web");

        // Initialize WebSocket before defining any routes
        Spark.webSocket("/ws", wsServer);

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", registerHandler::handleRequest);
        Spark.post("/session", loginHandler::handleRequest);
        Spark.delete("/db", clearHandler::handleRequest);
        Spark.delete("/session", logoutHandler::handleRequest);
        Spark.get("/game", listGamesHandler::handleRequest);
        Spark.post("/game", createGameHandler::handleRequest);
        Spark.put("/game", joinGameHandler::handleRequest);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}