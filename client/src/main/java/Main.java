import chess.BooleanWrapper;
import chess.ChessBoard;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import serverfacade.ServerFacade;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.out;
import static ui.DrawBoard.drawChessBoard;
import static ui.EscapeSequences.*;

public class Main {
    
    // Initialize scanner, booleans, and authTokens
    private static final ServerFacade FACADE = new ServerFacade();
    private static final String BASE_URL = "http://localhost:8080";
    private static final Scanner SCANNER = new Scanner(System.in);
    private static boolean loggedOut = true;
    private static String authToken = "";
    private static final HashMap<Integer, JsonElement> GAME_MAP = new HashMap<>();
    private static final BooleanWrapper QUIT = new BooleanWrapper(false);

    public static void main(String[] args) {


        out.println("Welcome to 240 chess. Type help to get started");

        // Begin log out options loop
        try {

            while (!QUIT.value) {
                if (loggedOut) {
                    loggedOutMenu();
                }

                // Begin the logged in options
                else {
                    loggedInMenu();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static void help(boolean loggedIn) {
        if (!loggedIn) {
            out.printf("register <USERNAME> <PASSWORD> <EMAIL> - to create an account %n" +
                    "login <USERNAME> <PASSWORD> - to play chess %n" + "quit - playing chess %n" +
                    "help - with possible commands %n"

            );
        } else {
            out.printf("create <NAME> - a game %n" + "list - games %n" +
                    "join <ID> [WHITE|BLACK] - a game %n" + "observe <ID> - a game %n" +
                    "logout - when you are done %n" + "quit - playing chess %n" +
                    "help - with possible commands %n");
        }
    }

    private static void list(String[] command, int commandLength) throws IOException {
        if (command.length != commandLength) {
            out.println("Incorrect amount of arguments");
        } else {

            JsonObject response = FACADE.listGames(authToken, BASE_URL);
            if (response.get("success").getAsBoolean()) {
                JsonArray gameArray = response.get("games").getAsJsonArray();
                for (int i = 1; i < gameArray.size() + 1; i++) {
                    GAME_MAP.put(i, gameArray.get(i - 1));
                }
                if (GAME_MAP.isEmpty()) {
                    out.println("No games");
                }
                for (Map.Entry<Integer, JsonElement> entry : GAME_MAP.entrySet()) {
                    JsonObject game = entry.getValue().getAsJsonObject();
                    JsonElement whiteElement = game.get("whiteUsername");
                    JsonElement blackElement = game.get("blackUsername");
                    String whiteUsername = "none";
                    String blackUsername = "none";
                    if (whiteElement != null) {
                        whiteUsername = whiteElement.getAsString();
                    }
                    if (blackElement != null) {
                        blackUsername = blackElement.getAsString();
                    }
                    out.println("Game number: " + entry.getKey() + ", game name: " +
                            game.get("gameName").getAsString()
                            + ", white username: " + whiteUsername
                            + ", black username: " + blackUsername);

                }

            }

        }
    }

    private static void loggedOutMenu() throws IOException {
        out.print("[LOGGED_OUT] >>> ");
        String[] command = SCANNER.nextLine().split(" ");
        while (command.length == 0) {
            out.print("[LOGGED_OUT] >>> ");
            command = SCANNER.nextLine().split(" ");
        }
        String baseCommand = command[0];
        JsonObject response;

        switch (baseCommand) {
            case "help":
                help(false);
                break;

            case "register":

                // Verify proper structure
                if (command.length != 4) {
                    out.println("Incorrect amount of arguments");
                    break;
                }

                response = FACADE.register(command[1], command[2], command[3], BASE_URL);
                if (response.get("success").getAsBoolean()) {
                    authToken = response.get("authToken").getAsString();
                    out.println("Logged in as " + command[1]);
                    loggedOut = false;
                }
                break;

            case "login":
                if (command.length != 3) {
                    out.println("Incorrect amount of arguments");
                    break;
                }

                response = FACADE.login(command[1], command[2], BASE_URL);
                if (response.get("success").getAsBoolean()) {
                    authToken = response.get("authToken").getAsString();
                    out.println("Logged in as " + command[1]);
                    loggedOut = false;
                } else {
                    out.println("failed to login");

                }
                break;

            case "quit":
                QUIT.value = true;
                break;
        }
    }

    private static void loggedInMenu() throws IOException {
        out.print("[LOGGED_IN] >>> ");
        String[] command = SCANNER.nextLine().split(" ");
        while (command.length == 0) {
            out.print("[LOGGED_IN] >>> ");
            command = SCANNER.nextLine().split(" ");
        }
        String baseCommand = command[0];
        JsonObject response;
        String gameID;

        int gameNumber;
        switch (baseCommand) {
            case "help":
                help(true);
                break;

            case "quit":
                QUIT.value = true;
                break;

            case "logout":
                response = FACADE.logout(authToken, BASE_URL);
                if (response.get("success").getAsBoolean()) {
                    out.println("Logged out");
                    authToken = "";
                    loggedOut = true;
                }
                break;

            case "create":
                if (command.length != 2) {
                    out.println("Incorrect amount of arguments");
                    break;
                }
                response = FACADE.createGame(command[1], authToken, BASE_URL);
                if (response.get("success").getAsBoolean()) {
                    out.println("Created game " + command[1]);
                }
                break;

            case "list":
                list(command, 1);
                break;

            case "join":
                if (GAME_MAP.isEmpty()) {
                    list(command, 3);
                    break;
                }
                if (command.length != 3) {
                    out.println("Incorrect amount of arguments");
                    break;
                }

                try {
                    gameNumber = Integer.parseInt(command[1]);
                } catch (Exception e) {
                    out.println("Could not parse game ID");
                    break;
                }

                if (gameNumber > GAME_MAP.size()) {
                    out.println("Game does not exist");
                    break;
                }

                if (!command[2].equals("WHITE") && !command[2].equals("BLACK")) {
                    out.println("Invalid color");
                    break;
                }

                gameID = GAME_MAP.get(gameNumber).getAsJsonObject().get("gameID").getAsString();

                response = FACADE.joinGame(command[2], gameID, authToken, BASE_URL);
                if (response.get("success").getAsBoolean()) {
                    out.println("Joined game with ID " + command[1] + " as color " + command[2]);
                } else {
                    out.println("Failed to join game");
                }
                break;

            case "observe":
                if (GAME_MAP.isEmpty()) {
                    list(command, 2);
                    break;
                }
                if (command.length != 2) {
                    out.println("Incorrect amount of arguments");
                    break;
                }
                try {
                    gameNumber = Integer.parseInt(command[1]);
                } catch (Exception e) {
                    out.println("Could not parse game ID");
                    break;
                }
                if (gameNumber > GAME_MAP.size()) {
                    out.println("Game does not exist");
                    break;
                }
                drawBoard();
                break;
        }
    }

    private static void drawBoard() {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        out.print(ERASE_SCREEN);
        drawChessBoard(out, board, false);
        drawChessBoard(out, board, true);
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void verifyArguments(int correctAmount) {
        out.println("Incorrect amount of arguments");

    }

}


