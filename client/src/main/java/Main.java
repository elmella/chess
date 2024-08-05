import chess.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import serverfacade.ServerFacade;
import ui.DrawBoard;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static ui.DrawBoard.*;
import static ui.EscapeSequences.*;

public class Main {
    public static void main(String[] args) {

        // Initialize scanner, booleans, and authTokens
        final Gson gson = new Gson();
        Scanner scanner = new Scanner(System.in);
        final ServerFacade s = new ServerFacade();
        final String baseURL = "http://localhost:8080";
        boolean loggedOut = true;
        boolean quit = false;
        String authToken = "";
        HashMap<Integer, JsonElement> gameMap = new HashMap<>();
        int gameNumber;
        String gameID;

        System.out.println("Welcome to 240 chess. Type help to get started");

        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);


        // Begin log out options loop
        while (!quit) {
            if (loggedOut) {
                System.out.print("[LOGGED_OUT] >>> ");
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
                            System.out.println("Incorrect amount of arguments");
                            break;
                        }

                        try {
                            JsonObject response = s.register(command[1], command[2], command[3], baseURL);
                            System.out.println(response);
                            if (response.get("success").getAsBoolean()) {
                                authToken = response.get("authToken").getAsString();
                                System.out.println("Logged in as " + command[1]);
                                loggedOut = false;
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;

                    case "login":
                        if (command.length != 3) {
                            System.out.println("Incorrect amount of arguments");
                            break;
                        }

                        try {
                            JsonObject response = s.login(command[1], command[2], baseURL);
                            System.out.println(response);
                            if (response.get("success").getAsBoolean()) {
                                authToken = response.get("authToken").getAsString();
                                System.out.println("Logged in as " + command[1]);
                                loggedOut = false;
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;

                    case "quit":
                        quit = true;
                        break;
                }
            }

            // Begin logged in options
            else {
                System.out.print("[LOGGED_IN] >>> ");
                String line = scanner.nextLine();
                String[] command = line.split(" ");
                String baseCommand = command[0];

                switch (baseCommand) {
                    case "help":
                        help(true);
                        break;

                    case "quit":
                        quit = true;
                        break;

                    case "create":
                        if (command.length != 2) {
                            System.out.println("Incorrect amount of arguments");
                            break;
                        }

                        try {
                            JsonObject response = s.createGame(command[1], authToken, baseURL);
                            System.out.println(response);
                            if (response.get("success").getAsBoolean()) {
                                System.out.println("Created game " + command[1]);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;

                    case "list":
                        if (command.length != 1) {
                            System.out.println("Incorrect amount of arguments");
                            break;
                        }

                        try {
                            JsonObject response = s.listGames(authToken, baseURL);
                            if (response.get("success").getAsBoolean()) {
                                JsonArray gameArray = response.get("games").getAsJsonArray();
                                for (int i = 1; i < gameArray.size() + 1; i++) {
                                    gameMap.put(i, gameArray.get(i - 1));
                                }
                                System.out.println(gameMap);

                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;


                    case "join":
                        if (command.length != 3) {
                            System.out.println("Incorrect amount of arguments");
                            break;
                        }

                        gameNumber = Integer.parseInt(command[2]);
                        gameID = gameMap.get(gameNumber).getAsJsonObject().get("gameID").getAsString();

                        try {
                            JsonObject response = s.joinGame(command[1], gameID, authToken, baseURL);
                            System.out.println(response);
                            if (response.get("success").getAsBoolean()) {
                                System.out.println("Joined game with ID " + command[1] + " as color " + command[2]);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;

                    case "observe":
                        if (command.length != 2) {
                            System.out.println("Incorrect amount of arguments");
                            break;
                        }

                        gameNumber = Integer.parseInt(command[1]);
                        gameID = gameMap.get(gameNumber).getAsJsonObject().get("gameID").getAsString();
                        ChessBoard board = new ChessBoard();
                        out.print(ERASE_SCREEN);

                        drawHeaders(out);
                        drawChessBoard(out, board);
                        drawHeaders(out);
                        

                        System.out.println(gameMap.get(gameNumber).getAsJsonObject());
                        break;
                }
            }
        }
    }


    private static void help(boolean loggedIn) {
        if (!loggedIn) {
            System.out.printf(
                    "register <USERNAME> <PASSWORD> <EMAIL> - to create an account %n" +
                            "login <USERNAME> <PASSWORD> - to play chess %n" +
                            "quit - playing chess %n" +
                            "help - with possible commands %n"

            );
        }
        else {
            System.out.printf(
                    "create <NAME> - a game %n" +
                            "list - games %n" +
                            "join <ID> [WHITE|BLACK] - a game %n" +
                            "observe <ID> - a game %n" +
                            "logout - when you are done %n" +
                            "quit - playing chess %n" +
                            "help - with possible commands"
            );
        }
    }

}