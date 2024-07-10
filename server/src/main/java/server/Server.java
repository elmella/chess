package server;

import dataaccess.*;
import handler.ClearHandler;
import handler.LoginHandler;
import handler.RegisterHandler;
import spark.*;

public class Server {

    private final LoginHandler loginHandler;
    private final RegisterHandler registerHandler;
    private ClearHandler clearHandler;

    public Server() {
        this.loginHandler = new LoginHandler();
        this.registerHandler = new RegisterHandler();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/session", loginHandler::handleRequest);
        Spark.post("/user", registerHandler::handleRequest);


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
