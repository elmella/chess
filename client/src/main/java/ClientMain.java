import chess.BooleanWrapper;
import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import serverfacade.ServerFacade;
import websocket.ServerMessageHandler;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.out;
import static ui.DrawBoard.drawChessBoard;
import static ui.EscapeSequences.*;

public class ClientMain {
    public static void main(String[] args) {
        String baseUrl = "http://localhost:8080";
        if (args.length == 1) {
            baseUrl = args[0];
        }

        new Navigate(baseUrl).run();

    }


//        out.println("Welcome to 240 chess. Type help to get started");
//        // Begin log out options loop
//        try {
//
//            while (!QUIT.value) {
//                if (loggedOut) {
//                    loggedOutMenu();
//                }
//
//                // Begin the logged in options
//                else {
//                    loggedInMenu();
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//
//    private static void help(boolean loggedIn) {
//        if (!loggedIn) {
//            out.printf("register <USERNAME> <PASSWORD> <EMAIL> - to create an account %n" +
//                    "login <USERNAME> <PASSWORD> - to play chess %n" + "quit - playing chess %n" +
//                    "help - with possible commands %n"
//
//            );
//        } else {
//            out.printf("create <NAME> - a game %n" + "list - games %n" +
//                    "join <ID> [WHITE|BLACK] - a game %n" + "observe <ID> - a game %n" +
//                    "logout - when you are done %n" + "quit - playing chess %n" +
//                    "help - with possible commands %n");
//        }
//    }
//
//    private static void list(String[] command, int commandLength) throws IOException {
//        if (!verifyArguments(command, commandLength)) {
//
//            JsonObject response = FACADE.listGames(authToken);
//            if (response.get("success").getAsBoolean()) {
//                JsonArray gameArray = response.get("games").getAsJsonArray();
//                for (int i = 1; i < gameArray.size() + 1; i++) {
//                    GAME_MAP.put(i, gameArray.get(i - 1));
//                }
//                if (GAME_MAP.isEmpty()) {
//                    out.println("No games");
//                }
//                for (Map.Entry<Integer, JsonElement> entry : GAME_MAP.entrySet()) {
//                    JsonObject game = entry.getValue().getAsJsonObject();
//                    JsonElement whiteElement = game.get("whiteUsername");
//                    JsonElement blackElement = game.get("blackUsername");
//                    String whiteUsername = "none";
//                    String blackUsername = "none";
//                    if (whiteElement != null) {
//                        whiteUsername = whiteElement.getAsString();
//                    }
//                    if (blackElement != null) {
//                        blackUsername = blackElement.getAsString();
//                    }
//                    out.println("Game number: " + entry.getKey() + ", game name: " +
//                            game.get("gameName").getAsString()
//                            + ", white username: " + whiteUsername
//                            + ", black username: " + blackUsername);
//
//                }
//
//            }
//
//        }
//    }
//
//    private static void loggedOutMenu() throws IOException {
//        out.print("[LOGGED_OUT] >>> ");
//        String[] command = SCANNER.nextLine().split(" ");
//        while (command.length == 0) {
//            out.print("[LOGGED_OUT] >>> ");
//            command = SCANNER.nextLine().split(" ");
//        }
//        String baseCommand = command[0];
//        JsonObject response;
//
//        switch (baseCommand) {
//            case "help":
//                help(false);
//                break;
//
//            case "register":
//                // Verify proper structure
//                if (verifyArguments(command, 4)) { break; }
//
//                response = FACADE.register(command[1], command[2], command[3]);
//                if (response.get("success").getAsBoolean()) {
//                    authToken = response.get("authToken").getAsString();
//                    out.println("Logged in as " + command[1]);
//                    loggedOut = false;
//                }
//                break;
//
//            case "login":
//                if (verifyArguments(command, 3)) { break; }
//
//
//                response = FACADE.login(command[1], command[2]);
//                if (response.get("success").getAsBoolean()) {
//                    authToken = response.get("authToken").getAsString();
//                    out.println("Logged in as " + command[1]);
//                    loggedOut = false;
//                } else {
//                    out.println("failed to login");
//
//                }
//                break;
//
//            case "quit":
//                QUIT.value = true;
//                break;
//        }
//    }
//
//    private static void loggedInMenu() throws IOException {
//        out.print("[LOGGED_IN] >>> ");
//        String[] command = SCANNER.nextLine().split(" ");
//        while (command.length == 0) {
//            out.print("[LOGGED_IN] >>> ");
//            command = SCANNER.nextLine().split(" ");
//        }
//        String baseCommand = command[0];
//        JsonObject response;
//        String gameID;
//
//        int gameNumber;
//        switch (baseCommand) {
//            case "help":
//                help(true);
//                break;
//
//            case "quit":
//                QUIT.value = true;
//                break;
//
//            case "logout":
//                response = FACADE.logout(authToken);
//                if (response.get("success").getAsBoolean()) {
//                    out.println("Logged out");
//                    authToken = "";
//                    loggedOut = true;
//                }
//                break;
//
//            case "create":
//                if (verifyArguments(command, 2)) { break; }
//                response = FACADE.createGame(command[1], authToken);
//                if (response.get("success").getAsBoolean()) {
//                    out.println("Created game " + command[1]);
//                }
//                break;
//
//            case "list":
//                list(command, 1);
//                break;
//
//            case "join":
//                if (GAME_MAP.isEmpty()) {
//                    list(command, 3);
//                    break;
//                }
//                if (verifyArguments(command, 3)) { break; }
//                try {
//                    gameNumber = Integer.parseInt(command[1]);
//                } catch (Exception e) {
//                    out.println("Could not parse game ID");
//                    break;
//                }
//
//                if (gameNumber > GAME_MAP.size()) {
//                    out.println("Game does not exist");
//                    break;
//                }
//
//                if (!command[2].equals("WHITE") && !command[2].equals("BLACK")) {
//                    out.println("Invalid color");
//                    break;
//                }
//
//                gameID = GAME_MAP.get(gameNumber).getAsJsonObject().get("gameID").getAsString();
//
//                response = FACADE.joinGame(command[2], gameID, authToken);
//                if (response.get("success").getAsBoolean()) {
//                    out.println("Joined game with ID " + command[1] + " as color " + command[2]);
//                } else {
//                    out.println("Failed to join game");
//                }
//                break;
//
//            case "observe":
//                if (GAME_MAP.isEmpty()) {
//                    list(command, 2);
//                    break;
//                }
//                if (verifyArguments(command, 2)) { break; }
//                try {
//                    gameNumber = Integer.parseInt(command[1]);
//                } catch (Exception e) {
//                    out.println("Could not parse game ID");
//                    break;
//                }
//                if (gameNumber > GAME_MAP.size()) {
//                    out.println("Game does not exist");
//                    break;
//                }
//                drawBoard();
//                break;
//        }
//    }
//
//    private static void drawBoard() {
//        ChessBoard board = new ChessBoard();
//        board.resetBoard();
//        out.print(ERASE_SCREEN);
//        drawChessBoard(out, board, false);
//        drawChessBoard(out, board, true);
//        out.print(SET_BG_COLOR_BLACK);
//        out.print(SET_TEXT_COLOR_WHITE);
//    }
//
//    private static boolean verifyArguments(String[] command, int correctAmount) {
//        if (command.length != correctAmount) {
//            out.println("Incorrect amount of arguments");
//            return true;
//        } else {
//            return false;
//        }
//
//    }
//
//    public void notify(ServerMessage message) {
//        switch (message.getServerMessageType()) {
//            case NOTIFICATION -> displayNotification(((NotificationMessage) message).getMessage());
//            case ERROR -> displayError(((ErrorMessage) message).getErrorMessage());
//            case LOAD_GAME -> loadGame(((LoadGameMessage) message).getGame());
//        }
//    }
//
//
//    private void displayNotification(String notification) {
//        System.out.println(notification);
//    }
//
//    private void displayError(String error) {
//        System.out.println(error);
//    }
//
//    private void loadGame(ChessGame game) {
//        ChessBoard board = game.getBoard();
//        board.resetBoard();
//        out.print(ERASE_SCREEN);
//        drawChessBoard(out, board, false);
//        drawChessBoard(out, board, true);
//        out.print(SET_BG_COLOR_BLACK);
//        out.print(SET_TEXT_COLOR_WHITE);
//    }

}


