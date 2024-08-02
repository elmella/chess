package server;

import dataaccess.*;
import handler.*;
import spark.*;

public class Server {

    private final LoginHandler loginHandler;
    private final RegisterHandler registerHandler;
    private final ClearHandler clearHandler;
    private final LogoutHandler logoutHandler;
    private final ListGamesHandler listGamesHandler;
    private final CreateGameHandler createGameHandler;
    private final JoinGameHandler joinGameHandler;

    public Server() {
        this.loginHandler = new LoginHandler();
        this.registerHandler = new RegisterHandler();
        this.clearHandler = new ClearHandler();
        this.logoutHandler = new LogoutHandler();
        this.listGamesHandler = new ListGamesHandler();
        this.createGameHandler = new CreateGameHandler();
        this.joinGameHandler = new JoinGameHandler();
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

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", registerHandler::handleRequest);
        Spark.post("/session", loginHandler::handleRequest);
        Spark.delete("/db", clearHandler::handleRequest);
        Spark.delete("/session", logoutHandler::handleRequest);
        Spark.get("/game", listGamesHandler::handleRequest);
        Spark.post("/game", createGameHandler::handleRequest);
        Spark.put("/game", joinGameHandler::handleRequest);



        //This line initializes the server and can be removed once you have a functioning endpoint 
//        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
