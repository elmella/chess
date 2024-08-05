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
import java.util.Scanner;

import static java.lang.System.out;
import static ui.DrawBoard.drawChessBoard;
import static ui.EscapeSequences.*;

public class Main {
    private static final ServerFacade FACADE = new ServerFacade();
    private static final String BASE_URL = "http://localhost:8080";
    private static final Scanner SCANNER = new Scanner(System.in);
    private static boolean loggedOut = true;
    private static String authToken = "";
    private static final HashMap<Integer, JsonElement> GAME_MAP = new HashMap<>();
    private static BooleanWrapper quit = new BooleanWrapper(false);
    // Initialize scanner, booleans, and authTokens
    private final Gson gson = new Gson();

    public static void main(String[] args) {


        out.println("Welcome to 240 chess. Type help to get started");

        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);


        // Begin log out options loop
        try {

            while (!quit.value) {
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

    private static void list(String[] command, int commandLength) {
        if (command.length != commandLength) {
            out.println("Incorrect amount of arguments");
        } else {

            try {
                JsonObject response = FACADE.listGames(authToken, BASE_URL);
                if (response.get("success").getAsBoolean()) {
                    JsonArray gameArray = response.get("games").getAsJsonArray();
                    for (int i = 1; i < gameArray.size() + 1; i++) {
                        GAME_MAP.put(i, gameArray.get(i - 1));
                    }
                    out.println(GAME_MAP);

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void loggedOutMenu() throws IOException {
        out.print("[LOGGED_OUT] >>> ");
        String line = SCANNER.nextLine();
        String[] command = line.split(" ");
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
                }
                break;

            case "quit":
                quit.value = true;
                break;
        }
    }

    private static void loggedInMenu() throws IOException {
        out.print("[LOGGED_IN] >>> ");
        String line = SCANNER.nextLine();
        String[] command = line.split(" ");
        String baseCommand = command[0];
        JsonObject response;
        String gameID;

        int gameNumber;
        switch (baseCommand) {
            case "help":
                help(true);
                break;

            case "quit":
                quit.value = true;
                break;

            case "logout":
                response = FACADE.logout(authToken, BASE_URL);
                out.println(response);
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
                out.println(response);
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
                gameID = GAME_MAP.get(gameNumber).getAsJsonObject().get("gameID").getAsString();

                response = FACADE.joinGame(command[1], gameID, authToken, BASE_URL);
                out.println(response);
                if (response.get("success").getAsBoolean()) {
                    out.println("Joined game with ID " + command[1] + " as color " + command[2]);
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

                gameID = GAME_MAP.get(gameNumber).getAsJsonObject().get("gameID").getAsString();
                ChessBoard board = new ChessBoard();
                board.resetBoard();
                out.println(board);
                out.print(ERASE_SCREEN);
                drawChessBoard(out, board, false);
                drawChessBoard(out, board, true);
                out.print(SET_BG_COLOR_BLACK);
                out.print(SET_TEXT_COLOR_WHITE);

                break;
        }
    }

}


