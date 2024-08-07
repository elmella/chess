package websocket;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import service.AuthService;
import spark.Request;
import spark.Response;
import websocket.commands.UserGameCommand;

public class WebsocketHandler {

    public void authorize(UserGameCommand command) throws UnauthorizedException, DataAccessException {

        String authToken = command.getAuthToken();
        AuthService auth = new AuthService(AuthDAO.getInstance());

        if (!auth.authorize(authToken)) {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    public String getUsername(UserGameCommand command) throws UnauthorizedException, DataAccessException {
        String authToken = command.getAuthToken();
        AuthService auth = new AuthService(AuthDAO.getInstance());

        return auth.getUsername(authToken);
    }
}
