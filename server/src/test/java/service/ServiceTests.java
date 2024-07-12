package service;

import dataaccess.*;
import model.GameData;
import org.junit.jupiter.api.*;
import passoff.model.*;
import request.CreateGameRequest;
import request.CreateGameResponse;
import request.LoginRequest;
import request.RegisterRequest;
import result.ListGamesResponse;
import result.LoginResponse;
import server.Server;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceTests {

    private final GameService game = new GameService(MemoryGameDAO.getInstance(), MemoryAuthDAO.getInstance());
    private final ClearService clear = new ClearService(MemoryAuthDAO.getInstance(), MemoryGameDAO.getInstance(), MemoryUserDAO.getInstance());
    private final UserService user = new UserService(MemoryUserDAO.getInstance(), MemoryAuthDAO.getInstance());
    private final AuthService auth = new AuthService(MemoryAuthDAO.getInstance());

    @AfterEach
    public void clear() {
        clear.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Register Success")
    public void registerSuccess() {
        RegisterRequest registerRequest = new RegisterRequest("emily", "email@email.com", "iambeautiful");
        // Register user
        try {
            LoginResponse registerResponse = user.register(registerRequest);
            Assertions.assertTrue(registerResponse.isSuccess());
        } catch (DataAccessException | AlreadyTakenException | UnauthorizedException | BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(2)
    @DisplayName("Register Failure")
    public void registerFailure() {
        RegisterRequest registerRequest = new RegisterRequest("emily", "email@email.com", "iambeautiful");

        // Register user
        try {
            user.register(registerRequest);
        } catch (DataAccessException | AlreadyTakenException | UnauthorizedException | BadRequestException e) {
            throw new RuntimeException(e);
        }

        // Try registering again, should fail
        Assertions.assertThrows(AlreadyTakenException.class, () -> {
            user.register(registerRequest);
        });
    }

    @Test
    @Order(3)
    @DisplayName("Login Success")
    public void loginSuccess() {
        RegisterRequest registerRequest = new RegisterRequest("emily", "email@email.com", "iambeautiful");
        LoginRequest loginRequest = new LoginRequest("emily", "iambeautiful");

        try {
            // Register user
            user.register(registerRequest);

            // Login and get username, should be the same
            Assertions.assertEquals(user.login(loginRequest).getUsername(), "emily");
        } catch (DataAccessException | AlreadyTakenException | UnauthorizedException | BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(4)
    @DisplayName("Login Failure")
    public void loginFailure() {
        RegisterRequest registerRequest = new RegisterRequest("emily", "email@email.com", "iambeautiful");
        LoginRequest loginRequest = new LoginRequest("emily", "iamgorgeous");

        // Register user
        try {
            user.register(registerRequest);
        } catch (DataAccessException | AlreadyTakenException | UnauthorizedException | BadRequestException e) {
            throw new RuntimeException(e);
        }

        // Try logging in, wrong password, should throw unauthorized
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            user.login(loginRequest);
        });
    }

    @Test
    @Order(5)
    @DisplayName("Logout Success")
    public void logoutSuccess() {
        RegisterRequest registerRequest = new RegisterRequest("emily", "email@email.com", "iambeautiful");
        LoginRequest loginRequest = new LoginRequest("emily", "iambeautiful");

        try {
            // Register user
            user.register(registerRequest);

            // Log in user
            LoginResponse response = user.login(loginRequest);
            String authToken = response.getAuthToken();

            // Attempt to authorize, should return true
            boolean preLogout = auth.authorize(authToken);

            // Log out user
            user.logout(authToken);

            // Attempt to authorize, should return false
            boolean postLogout = auth.authorize(authToken);

            Assertions.assertTrue(preLogout && !postLogout);

        } catch (DataAccessException | AlreadyTakenException | UnauthorizedException | BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(6)
    @DisplayName("Logout Failure")
    public void logoutFailure() {
        RegisterRequest registerRequest = new RegisterRequest("emily", "email@email.com", "iambeautiful");
        LoginRequest loginRequest = new LoginRequest("emily", "iambeautiful");

        try {
            // Register user
            user.register(registerRequest);

            // Log in user
            LoginResponse response = user.login(loginRequest);
            String authToken = response.getAuthToken();

            // Attempt to authorize, should return true
            boolean preLogout = auth.authorize(authToken);

            // Log out user
            user.logout(authToken);

            // Attempt to log out again, should throw UnauthorizedException
            Assertions.assertThrows(UnauthorizedException.class, () -> {
                user.logout(authToken);
            });

        } catch (DataAccessException | AlreadyTakenException | UnauthorizedException | BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(7)
    @DisplayName("Create Game Success")
    public void createGameSuccess() {
        try {

            CreateGameRequest gameRequest = new CreateGameRequest("game");

            // Create game
            game.createGame(gameRequest);

            // List games
            ListGamesResponse games = game.getGames();

            // Verify games is not empty
            Assertions.assertFalse(games.getGameResponses().isEmpty());

        } catch (DataAccessException | BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(8)
    @DisplayName("Create Game Failure")
    public void createGameFailure() {
        try {

            CreateGameRequest gameRequest = new CreateGameRequest(null);

            // Create game
            game.createGame(gameRequest);

            // List games
            ListGamesResponse games = game.getGames();

            // Verify games is not empty
            Assertions.assertFalse(games.getGameResponses().isEmpty());

        } catch (DataAccessException | BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(10)
    @DisplayName("Clear success")
    public void clearSuccess() {
        // Add a game
        CreateGameRequest gameRequest = new CreateGameRequest("game");
        try {
        game.createGame(gameRequest);

        // Add a user, will also create an auth
        RegisterRequest userRequest = new RegisterRequest("username", "email@email.com", "password");
            user.register(userRequest);


        // Clear the data
        clear.clear();

        // Try to log in, should fail
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            user.login(new LoginRequest("username", "password"));
        });

        // Get games, should be empty
        Assertions.assertTrue(game.getGames().getGameResponses().isEmpty());
        } catch (DataAccessException | AlreadyTakenException | UnauthorizedException | BadRequestException e) {
            throw new RuntimeException(e);
        }

        // Re-register, should work
    }
}