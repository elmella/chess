package client;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.*;
import result.ListGamesResponse;
import server.Server;
import serverfacade.ServerFacade;

import java.io.IOException;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    private final String username = "username";
    private final String password = "password";
    private final String email = "email";
    private static final String baseURL = "http://localhost:8080";

    @BeforeAll
    public static void init() throws IOException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade();
        facade.clear(baseURL);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @Order(1)
    @DisplayName("Register Success")
    public void registerSuccess() throws IOException {

        JsonObject response = facade.register(username, password, email, baseURL);

        // Assert successful
        Assertions.assertTrue(response.get("success").getAsBoolean());
    }

    @Test
    @Order(2)
    @DisplayName("Register Failure")
    public void registerFailure() throws IOException {

        JsonObject response = facade.register(username, null, email, baseURL);

        // Assert not successful
        Assertions.assertFalse(response.get("success").getAsBoolean());
    }

    @Test
    @Order(3)
    @DisplayName("Login Success")
    public void loginSuccess() throws IOException {

        JsonObject response = facade.login(username, password, baseURL);

        // Assert successful
        Assertions.assertTrue(response.get("success").getAsBoolean());
    }

    @Test
    @Order(4)
    @DisplayName("Login Failure")
    public void loginFailure() throws IOException {

        JsonObject response = facade.login(username, null, baseURL);

        // Assert not successful
        Assertions.assertFalse(response.get("success").getAsBoolean());
    }

    @Test
    @Order(5)
    @DisplayName("Logout Success")
    public void logoutSuccess() throws IOException {

        // login
        JsonObject loginResponse = facade.login(username, password, baseURL);

        // logout
        JsonObject logoutResponse  = facade.logout(loginResponse.get("authToken").getAsString(), baseURL);

        // Assert successful
        Assertions.assertTrue(logoutResponse.get("success").getAsBoolean());
    }

    @Test
    @Order(6)
    @DisplayName("Login Failure")
    public void logoutFailure() throws IOException {

        // login
        JsonObject loginResponse = facade.login(username, password, baseURL);

        // logout, wrong authToken
        JsonObject logoutResponse  = facade.logout("wrong authToken", baseURL);

        // Assert not successful
        Assertions.assertFalse(logoutResponse.get("success").getAsBoolean());
    }


    @Test
    @Order(7)
    @DisplayName("List Games Success")
    public void listGamesSuccess() throws IOException {

        // login
        JsonObject loginResponse = facade.login(username, password, baseURL);

        // listGames
        JsonObject listGamesResponse = facade.listGames(loginResponse.get("authToken").getAsString(), baseURL);

        // Assert successful
        Assertions.assertTrue(listGamesResponse.get("success").getAsBoolean());
    }

    @Test
    @Order(8)
    @DisplayName("List Games Failure")
    public void listGamesFailure() throws IOException {

        // login
        JsonObject loginResponse = facade.login(username, password, baseURL);

        // listGames
        JsonObject listGamesResponse = facade.listGames("wrong authToken", baseURL);

        // Assert not successful
        Assertions.assertFalse(listGamesResponse.get("success").getAsBoolean());
    }

    @Test
    @Order(9)
    @DisplayName("Create Game Success")
    public void createGameSuccess() throws IOException {

        // login
        JsonObject loginResponse = facade.login(username, password, baseURL);

        // Create game
        JsonObject createGameResponse = facade.createGame("game", loginResponse.get("authToken").getAsString(), baseURL);

        // listGames
        JsonObject listGamesResponse = facade.listGames(loginResponse.get("authToken").getAsString(), baseURL);

        // Assert not empty
        Assertions.assertFalse(listGamesResponse.get("games").getAsJsonArray().isEmpty());
    }

    @Test
    @Order(10)
    @DisplayName("Create Game Failure")
    public void createGameFailure() throws IOException {

        // Login
        JsonObject loginResponse = facade.login(username, password, baseURL);

        // Create game
        JsonObject createGameResponse = facade.createGame("game", "wrong authToken", baseURL);

        // Assert not successful
        Assertions.assertFalse(createGameResponse.get("success").getAsBoolean());
    }

    @Test
    @Order(11)
    @DisplayName("Join Game Success")
    public void joinGameSuccess() throws IOException {

        // login
        JsonObject loginResponse = facade.login(username, password, baseURL);

        // Create game
        JsonObject createGameResponse = facade.createGame("game", loginResponse.get("authToken").getAsString(), baseURL);

        // listGames
        JsonObject listGamesResponse = facade.listGames(loginResponse.get("authToken").getAsString(), baseURL);

        // Get gameID
        String gameID = listGamesResponse.get("games").getAsJsonArray().get(0).getAsJsonObject().get("gameID").getAsString();

        // Join game as white
        JsonObject joinGameResponse = facade.joinGame("WHITE", gameID, loginResponse.get("authToken").getAsString(), baseURL);

        // Assert successful
        Assertions.assertTrue(joinGameResponse.get("success").getAsBoolean());
    }
    



}
