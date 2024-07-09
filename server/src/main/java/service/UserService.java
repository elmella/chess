package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import result.LoginResponse;

import java.util.UUID;

public class UserService {
    public LoginResponse register(UserData user) throws DataAccessException {
        // Declare DAO objects
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        // Check if user exists
        UserData foundUser = userDAO.getUser(user.username(), user.password());

        if (foundUser == null) {
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
        // Declare DAO objects
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
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

    public void logout(UserData user) {
        return;
    }
}
