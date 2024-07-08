package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;

public class UserService {
    public AuthData register(UserData user) throws DataAccessException {
        // Declare DAO objects
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        // Check if user exists
        
        // Create new user
        userDAO.createUser(user);


    }

    public AuthData login(UserData user) {
    }

    public void logout(UserData user) {
    }
}
