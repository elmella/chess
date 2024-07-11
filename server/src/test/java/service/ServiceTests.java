package service;

import dataaccess.*;
import org.junit.jupiter.api.*;
import passoff.model.*;
import request.CreateGameRequest;
import request.LoginRequest;
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
        } catch (DataAccessException | AlreadyTakenException e) {
            throw new RuntimeException(e);
        }

        // Clear the data
        clear.clear();

        // Try to log in, should fail
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            user.login(new LoginRequest("username", "password"));
        });
    }
}