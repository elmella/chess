package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    public AuthData register(UserData user) throws DataAccessException {
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
            return auth;
        }
        // Create new user

        return new AuthData(null,null);
    }

    public AuthData login(UserData user) {
        return null;
    }

    public void logout(UserData user) {
        return;
    }
}
