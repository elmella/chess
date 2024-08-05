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

        Assertions.assertTrue(response.get("success").getAsBoolean());
    }

    @Test
    @Order(2)
    @DisplayName("Register Failure")
    public void registerFailure() throws IOException {

        JsonObject response = facade.register(username, null, email, baseURL);

        Assertions.assertFalse(response.get("success").getAsBoolean());
    }

    @Test
    @Order(3)
    @DisplayName("Login Success")
    public void loginSuccess() throws IOException {

        JsonObject response = facade.login(username, password, baseURL);

        Assertions.assertTrue(response.get("success").getAsBoolean());
    }

    @Test
    @Order(4)
    @DisplayName("Login Failure")
    public void loginFailure() throws IOException {

        JsonObject response = facade.login(username, null, baseURL);

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
        Assertions.assertFalse(listGamesResponse.get("success").getAsBoolean());
    }



}
