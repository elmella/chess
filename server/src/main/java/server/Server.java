package server;

import dataaccess.*;
import handler.ClearHandler;
import handler.LoginHandler;
import handler.LogoutHandler;
import handler.RegisterHandler;
import spark.*;

public class Server {

    private final LoginHandler loginHandler;
    private final RegisterHandler registerHandler;
    private final ClearHandler clearHandler;
    private final LogoutHandler logoutHandler;

    public Server() {
        this.loginHandler = new LoginHandler();
        this.registerHandler = new RegisterHandler();
        this.clearHandler = new ClearHandler();
        this.logoutHandler = new LogoutHandler();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/session", loginHandler::handleRequest);
        Spark.post("/user", registerHandler::handleRequest);
        Spark.delete("/db", clearHandler::handleRequest);
        Spark.delete("/session", logoutHandler::handleRequest);


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
