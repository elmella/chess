package service;

import dataaccess.*;
import org.junit.jupiter.api.*;
import request.*;
import request.CreateGameResponse;
import result.ListGamesResponse;
import result.LoginResponse;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceTests {

    private final GameService game = new GameService(GameDAO.getInstance());
    private final ClearService clear = new ClearService(AuthDAO.getInstance(), GameDAO.getInstance(), UserDAO.getInstance());
    private final UserService user = new UserService(UserDAO.getInstance(), AuthDAO.getInstance());
    private final AuthService auth = new AuthService(AuthDAO.getInstance());

    @BeforeEach
    public void clear() throws DataAccessException {
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
        Assertions.assertThrows(AlreadyTakenException.class, () -> user.register(registerRequest));
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
        Assertions.assertThrows(UnauthorizedException.class, () -> user.login(loginRequest));
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
            auth.logout(authToken);

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

            // Log out user
            auth.logout(authToken);

            // Attempt to log out again, should throw UnauthorizedException
            Assertions.assertThrows(UnauthorizedException.class, () -> auth.logout(authToken));

        } catch (DataAccessException | AlreadyTakenException | UnauthorizedException | BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(7)
    @DisplayName("Create Game Success")
    public void createGameSuccess() {
        CreateGameRequest gameRequest = new CreateGameRequest("game");

        // Verify createGame does not throw any errors
        Assertions.assertDoesNotThrow(() -> game.createGame(gameRequest));
    }

    @Test
    @Order(8)
    @DisplayName("Create Game Failure")
    public void createGameFailure() {
        // Initialize game with null name
        CreateGameRequest gameRequest = new CreateGameRequest(null);

        // Attempt to create game, should throw a bad request
        Assertions.assertThrows(BadRequestException.class, () -> game.createGame(gameRequest));
    }

    @Test
    @Order(9)
    @DisplayName("List Games Success")
    public void listGamesSuccess() {
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
    @Order(10)
    @DisplayName("List Games Failure")
    public void listGamesFailure() {
        try {

            // Verify games is not empty
            Assertions.assertTrue(game.getGames().getGameResponses().isEmpty());

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(11)
    @DisplayName("Join Game Success")
    public void joinGameSuccess() {
        try {
            // Register, save authToken
            RegisterRequest registerRequest = new RegisterRequest("emily", "email@email.com", "iambeautiful");
            LoginResponse loginResponse = user.register(registerRequest);
            String authToken = loginResponse.getAuthToken();

            // Add a game
            CreateGameRequest gameRequest = new CreateGameRequest("game");
            // Create game
            CreateGameResponse gameResponse = game.createGame(gameRequest);

            int gameID = gameResponse.getGameID();

            // Join game
            JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", gameID);
            game.joinGame(joinGameRequest, authToken);

            // List games, verify username saved
            ListGamesResponse listGamesResponse = game.getGames();
            Assertions.assertEquals(listGamesResponse.getGameResponses().getFirst().whiteUsername(), "emily");

        } catch (DataAccessException | AlreadyTakenException | UnauthorizedException | BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(12)
    @DisplayName("Join Game Failure")
    public void joinGameFailure() {
        try {
            // Register, save authToken, do twice
            RegisterRequest registerRequest = new RegisterRequest("emily", "email@email.com", "iambeautiful");
            RegisterRequest registerRequest2 = new RegisterRequest("matt", "email@email.com", "iamlessbeautiful");
            LoginResponse loginResponse = user.register(registerRequest);
            LoginResponse loginResponse2 = user.register(registerRequest2);
            String authToken = loginResponse.getAuthToken();
            String authToken2 = loginResponse2.getAuthToken();


            // Add a game
            CreateGameRequest gameRequest = new CreateGameRequest("game");

            // Create game
            CreateGameResponse gameResponse = game.createGame(gameRequest);

            int gameID = gameResponse.getGameID();

            // Join game
            JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", gameID);
            game.joinGame(joinGameRequest, authToken);

            // Attempt to join game as same color, should fail
            JoinGameRequest joinGameRequest2 = new JoinGameRequest("WHITE", gameID);
            Assertions.assertThrows(AlreadyTakenException.class, () -> game.joinGame(joinGameRequest2, authToken2));

        } catch (DataAccessException | AlreadyTakenException | UnauthorizedException | BadRequestException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @Order(13)
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
            Assertions.assertThrows(UnauthorizedException.class, () -> user.login(new LoginRequest("username", "password")));

            // Get games, should be empty
            Assertions.assertTrue(game.getGames().getGameResponses().isEmpty());
        } catch (DataAccessException | AlreadyTakenException | UnauthorizedException | BadRequestException e) {
            throw new RuntimeException(e);
        }

    }
}