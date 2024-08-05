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
    // Initialize scanner, booleans, and authTokens
    private final Gson gson = new Gson();
    private static Scanner scanner = new Scanner(System.in);
    private static final ServerFacade s = new ServerFacade();
    private static final String baseURL = "http://localhost:8080";
    private static boolean loggedOut = true;
    private static String authToken = "";
    private static HashMap<Integer, JsonElement> gameMap = new HashMap<>();
    private static int gameNumber;
    private static String gameID;
    private static BooleanWrapper quit = new BooleanWrapper(false);

    public static void main(String[] args) {


        out.println("Welcome to 240 chess. Type help to get started");

        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);


        // Begin log out options loop
        while (!quit.value) {
            if (loggedOut) {
                loggedOutMenu();
            }

            // Begin logged in options
            else {
                loggedInMenu();
            }
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

    private static void list(String[] command, ServerFacade s, String authToken,
                             String baseURL, HashMap<Integer, JsonElement> gameMap, int commandLength) {
        if (command.length != commandLength) {
            out.println("Incorrect amount of arguments");
        } else {

            try {
                JsonObject response = s.listGames(authToken, baseURL);
                if (response.get("success").getAsBoolean()) {
                    JsonArray gameArray = response.get("games").getAsJsonArray();
                    for (int i = 1; i < gameArray.size() + 1; i++) {
                        gameMap.put(i, gameArray.get(i - 1));
                    }
                    out.println(gameMap);

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void loggedOutMenu() {
        out.print("[LOGGED_OUT] >>> ");
        String line = scanner.nextLine();
        String[] command = line.split(" ");
        String baseCommand = command[0];

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

                try {
                    JsonObject response = s.register(command[1], command[2], command[3], baseURL);
                    out.println(response);
                    if (response.get("success").getAsBoolean()) {
                        authToken = response.get("authToken").getAsString();
                        out.println("Logged in as " + command[1]);
                        loggedOut = false;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;

            case "login":
                if (command.length != 3) {
                    out.println("Incorrect amount of arguments");
                    break;
                }

                try {
                    JsonObject response = s.login(command[1], command[2], baseURL);
                    if (response.get("success").getAsBoolean()) {
                        authToken = response.get("authToken").getAsString();
                        out.println("Logged in as " + command[1]);
                        loggedOut = false;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;

            case "quit":
                quit.value = true;
                break;
        }
    }

    private static void loggedInMenu() {
        out.print("[LOGGED_IN] >>> ");
        String line = scanner.nextLine();
        String[] command = line.split(" ");
        String baseCommand = command[0];

        switch (baseCommand) {
            case "help":
                help(true);
                break;

            case "quit":
                quit.value = true;
                break;

            case "logout":
                try {
                    JsonObject response = s.logout(authToken, baseURL);
                    out.println(response);
                    if (response.get("success").getAsBoolean()) {
                        out.println("Logged out");
                        authToken = "";
                        loggedOut = true;
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;

            case "create":
                if (command.length != 2) {
                    out.println("Incorrect amount of arguments");
                    break;
                }

                try {
                    JsonObject response = s.createGame(command[1], authToken, baseURL);
                    out.println(response);
                    if (response.get("success").getAsBoolean()) {
                        out.println("Created game " + command[1]);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;

            case "list":
                list(command, s, authToken, baseURL, gameMap, 1);
                break;


            case "join":
                if (gameMap.isEmpty()) {
                    list(command, s, authToken, baseURL, gameMap, 3);
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

                if (gameNumber > gameMap.size()) {
                    out.println("Game does not exist");
                    break;
                }

                gameID = gameMap.get(gameNumber).getAsJsonObject().get("gameID").getAsString();

                try {
                    JsonObject response = s.joinGame(command[1], gameID, authToken, baseURL);
                    out.println(response);
                    if (response.get("success").getAsBoolean()) {
                        out.println("Joined game with ID " + command[1] + " as color " + command[2]);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;

            case "observe":
                if (gameMap.isEmpty()) {
                    list(command, s, authToken, baseURL, gameMap, 2);
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

                if (gameNumber > gameMap.size()) {
                    out.println("Game does not exist");
                    break;
                }

                gameID = gameMap.get(gameNumber).getAsJsonObject().get("gameID").getAsString();
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


