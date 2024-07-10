package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResponse;
import result.Response;

import java.util.UUID;

public class UserService {
    private final UserDAOInterface userDAO;
    private final AuthDAOInterface authDAO;

    public UserService(UserDAOInterface userDAO, AuthDAOInterface authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }


    public LoginResponse register(RegisterRequest request) throws DataAccessException {
        // Check if user exists
        UserData foundUser = userDAO.getUser(request.username(), request.password());

        if (foundUser == null) {
            UserData user = new UserData(request.username(), request.password(), request.email());
            userDAO.createUser(user);
            String authToken = UUID.randomUUID().toString();
            String username = user.username();
            AuthData auth = new AuthData(authToken, username);
            authDAO.createAuth(auth);
            return new LoginResponse(authToken, username, true, null);
        }
        // Create new user

        return new LoginResponse(null, null, false, "Error: already taken");
    }

    public LoginResponse login(LoginRequest request) throws DataAccessException {
        String username = request.username();
        // Check if user exists
        UserData foundUser = userDAO.getUser(username, request.password());

        if (foundUser != null) {
            String authToken = UUID.randomUUID().toString();
            AuthData auth = new AuthData(authToken, username);
            authDAO.createAuth(auth);
            return new LoginResponse(authToken, username, true, null);
        }
        // Create new user

        return new LoginResponse(null, null, false, "Error: unauthorized");
    }

    public Response logout(String authToken) {
        try {
            AuthData auth = authDAO.getAuth(authToken);
            if (auth == null) {
                return new Response("Error: unauthorized", false);
            }
            authDAO.deleteAuth(auth);

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return new Response(null, true);
    }
}
