package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.*;
import passoff.model.*;
import request.CreateGameRequest;
import request.RegisterRequest;
import server.Server;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceTests {

    private static TestUser existingUser;

    private static TestUser newUser;

    private static TestCreateRequest createRequest;

    private static Server server;

    private String existingAuth;

    @Test
    @Order(1)
    @DisplayName("Clear")
    public void clearSuccess() {
        // Create service objects
        GameService game = new GameService(MemoryGameDAO.getInstance(), MemoryAuthDAO.getInstance());
        ClearService clear = new ClearService(MemoryAuthDAO.getInstance(), MemoryGameDAO.getInstance(), MemoryUserDAO.getInstance());
        UserService user = new UserService(MemoryUserDAO.getInstance(), MemoryAuthDAO.getInstance());

        // Add a game
        CreateGameRequest gameRequest = new CreateGameRequest("game");
        game.createGame(gameRequest);

        // Add a user, will also create an auth
        RegisterRequest userRequest = new RegisterRequest("username", "email@email.com", "password");
        try {
            user.register(userRequest);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        clear.clear();

//        Assertions.assertEquals();
    }
}