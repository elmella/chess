package client;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.*;
import server.Server;
import serverfacade.ServerFacade;

import java.io.IOException;


public class ServerFacadeTests {

    static ServerFacade facade;
    private static Server server;
    private final String username = "username";
    private final String password = "password";
    private final String email = "email";

    @BeforeAll
    public static void init() throws IOException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String baseUrl = "http://localhost:" + port;
        facade = new ServerFacade(baseUrl);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clear() throws IOException {
        facade.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Register Success")
    public void registerSuccess() throws IOException {

        JsonObject response = facade.register(username, password, email);

        // Assert successful
        Assertions.assertTrue(response.get("success").getAsBoolean());
    }

    @Test
    @Order(2)
    @DisplayName("Register Failure")
    public void registerFailure() throws IOException {

        JsonObject response = facade.register(username, null, email);

        // Assert not successful
        Assertions.assertFalse(response.get("success").getAsBoolean());
    }

    @Test
    @Order(3)
    @DisplayName("Login Success")
    public void loginSuccess() throws IOException {

        // Register
        facade.register(username, password, email);

        // Login
        JsonObject response = facade.login(username, password);

        // Assert successful
        Assertions.assertTrue(response.get("success").getAsBoolean());
    }

    @Test
    @Order(4)
    @DisplayName("Login Failure")
    public void loginFailure() throws IOException {

        // Register
        facade.register(username, password, email);

        // Login
        JsonObject response = facade.login(username, null);

        // Assert not successful
        Assertions.assertFalse(response.get("success").getAsBoolean());
    }

    @Test
    @Order(5)
    @DisplayName("Logout Success")
    public void logoutSuccess() throws IOException {

        // Register
        JsonObject registerResponse = facade.register(username, password, email);
        String authToken = registerResponse.get("authToken").getAsString();

        // logout
        JsonObject logoutResponse = facade.logout(authToken);

        // Assert successful
        Assertions.assertTrue(logoutResponse.get("success").getAsBoolean());
    }

    @Test
    @Order(6)
    @DisplayName("Login Failure")
    public void logoutFailure() throws IOException {

        // Register
        JsonObject registerResponse = facade.register(username, password, email);

        // logout, wrong authToken
        JsonObject logoutResponse = facade.logout("wrong authToken");

        // Assert not successful
        Assertions.assertFalse(logoutResponse.get("success").getAsBoolean());
    }


    @Test
    @Order(7)
    @DisplayName("List Games Success")
    public void listGamesSuccess() throws IOException {

        // Register
        JsonObject registerResponse = facade.register(username, password, email);
        String authToken = registerResponse.get("authToken").getAsString();

        // listGames
        JsonObject listGamesResponse = facade.listGames(authToken);

        // Assert successful
        Assertions.assertTrue(listGamesResponse.get("success").getAsBoolean());
    }

    @Test
    @Order(8)
    @DisplayName("List Games Failure")
    public void listGamesFailure() throws IOException {

        // Register
        JsonObject registerResponse = facade.register(username, password, email);

        // listGames
        JsonObject listGamesResponse = facade.listGames("wrong authToken");

        // Assert not successful
        Assertions.assertFalse(listGamesResponse.get("success").getAsBoolean());
    }

    @Test
    @Order(9)
    @DisplayName("Create Game Success")
    public void createGameSuccess() throws IOException {

        // Register
        JsonObject registerResponse = facade.register(username, password, email);
        String authToken = registerResponse.get("authToken").getAsString();

        // Create game
        JsonObject createGameResponse = facade.createGame("game", authToken);

        // listGames
        JsonObject listGamesResponse = facade.listGames(authToken);

        // Assert not empty
        Assertions.assertFalse(listGamesResponse.get("games").getAsJsonArray().isEmpty());
    }

    @Test
    @Order(10)
    @DisplayName("Create Game Failure")
    public void createGameFailure() throws IOException {

        // Register
        JsonObject registerResponse = facade.register(username, password, email);

        // Create game
        JsonObject createGameResponse = facade.createGame("game", "wrong authToken");

        // Assert not successful
        Assertions.assertFalse(createGameResponse.get("success").getAsBoolean());
    }

    @Test
    @Order(11)
    @DisplayName("Join Game Success")
    public void joinGameSuccess() throws IOException {

        // Register
        JsonObject registerResponse = facade.register(username, password, email);
        String authToken = registerResponse.get("authToken").getAsString();

        // Create game
        JsonObject createGameResponse = facade.createGame("game", authToken);

        // listGames
        JsonObject listGamesResponse = facade.listGames(authToken);

        // Get gameID
        String gameID = listGamesResponse.get("games").getAsJsonArray().get(0).getAsJsonObject().get("gameID").getAsString();

        // Join game as white
        JsonObject joinGameResponse = facade.joinGame("WHITE", gameID, authToken);

        // Assert successful
        Assertions.assertTrue(joinGameResponse.get("success").getAsBoolean());
    }


    @Test
    @Order(12)
    @DisplayName("Join Game Failure")
    public void joinGameFailure() throws IOException {

        // Register
        JsonObject registerResponse = facade.register(username, password, email);
        String authToken = registerResponse.get("authToken").getAsString();

        // Create game
        JsonObject createGameResponse = facade.createGame("game", authToken);

        // listGames
        JsonObject listGamesResponse = facade.listGames(authToken);

        // Get gameID
        String gameID = listGamesResponse.get("games").getAsJsonArray().get(0).getAsJsonObject().get("gameID").getAsString();

        // Join game as white
        JsonObject joinGameResponse = facade.joinGame("WHITE", gameID, "wrong authToken");

        // Assert failure
        Assertions.assertFalse(joinGameResponse.get("success").getAsBoolean());
    }

    @Test
    @Order(13)
    @DisplayName("Clear Success")
    public void clearSuccess() throws IOException {

        // Register
        JsonObject registerResponse = facade.register(username, password, email);
        String authToken = registerResponse.get("authToken").getAsString();

        // Create game
        JsonObject createGameResponse = facade.createGame("game", authToken);

        facade.clear();

        // listGames
        JsonObject listGamesResponse = facade.listGames(authToken);

        // Assert empty
        Assertions.assertNull(listGamesResponse.get("games"));
    }

    @Test
    @Order(14)
    @DisplayName("Clear Failure")
    public void clearFailure() throws IOException {

        // Register
        JsonObject registerResponse = facade.register(username, password, email);
        String authToken = registerResponse.get("authToken").getAsString();

        facade.clear();

        // Login
        JsonObject response = facade.login(username, password);

        // Assert not empty and logged in
        Assertions.assertFalse(response.get("success").getAsBoolean());
    }


}
