import chess.ChessBoard;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import serverfacade.ServerFacade;
import serverfacade.WebSocketFacade;
import websocket.ServerMessageHandler;
import websocket.exception.ResponseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.out;
import static ui.DrawBoard.drawChessBoard;
import static ui.EscapeSequences.*;

public class Client {
    private final ServerFacade facade;
    private WebSocketFacade wsFacade;
    private final String baseUrl;
    private final ServerMessageHandler serverMessageHandler;
    private String authToken;
    private Integer gameID;
    private boolean loggedIn;
    private boolean player;
    private boolean inGameplay;
    private final Gson gson = new Gson();
    private final HashMap<Integer, JsonElement> gameMap = new HashMap<>();

    public Client(String baseUrl, ServerMessageHandler serverMessageHandler) {
        this.baseUrl = baseUrl;
        this.serverMessageHandler = serverMessageHandler;
        facade = new ServerFacade(baseUrl);
        loggedIn = false;
        inGameplay = false;
        player = false;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isInGameplay() {
        return inGameplay;
    }

    public void setInGameplay(boolean inGameplay) {
        this.inGameplay = inGameplay;
    }

    public boolean isPlayer() {
        return player;
    }

    public void setPlayer(boolean player) {
        this.player = player;
    }

    public String loggedOutMenu(String input) throws ResponseException, IOException {
        String[] command = input.toLowerCase().split(" ");
        String baseCommand = command[0];
        return switch (baseCommand) {
            case "login" -> login(command);
            case "register" -> register(command);
            case "quit" -> "quit";
            default -> loggedOutHelp();
        };
    }

    public String loggedInMenu(String input) throws ResponseException, IOException {
        String[] command = input.toLowerCase().split(" ");
        String baseCommand = command[0];
        return switch (baseCommand) {
            case "create" -> create(command);
            case "list" -> list(command);
            case "join" -> join(command);
            case "observe" -> observe(command);
            case "logout" -> logout(command);
            case "quit" -> "quit";
            default -> loggedInHelp();
        };
    }

    public String gamePlayMenu(String input) throws IOException {
        String[] command = input.toLowerCase().split(" ");
        String baseCommand = command[0];
        return switch (baseCommand) {
            case "move" -> move(command);
            case "leave" -> leave(command);
            case "resign" -> resign(command);
            case "redraw" -> redraw(command);
            case "highlight" -> highlight(command);
            default -> gamePlayHelp();
        };
    }

    public String loggedOutHelp() {
            return """
                    \n
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account\s
                    login <USERNAME> <PASSWORD> - to play chess \s
                    quit - playing chess\s
                    help - with possible commands\s
                    """;
    }

    public String loggedInHelp() {
        return """
                \n
                create <NAME> - a game\s
                list - games\s
                join <ID> [WHITE|BLACK] - a game\s
                observe <ID> - a game\s
                logout - when you are done\s
                quit - playing chess\s
                help - with possible commands\s
                """;
    }

    public String gamePlayHelp() {
        if (player) {
            return """
                    \n
                    move <START_ROW> <START_COL> <END_ROW> <END_COL> <PROMOTION_PIECE_TYPE> (If applicable) 
                    - make move (move 7 a 8 a KING)\s
                    leave - leave the game\s
                    resign - end the game and lose\s
                    redraw - redraws the board\s
                    highlight <ROW> <COL> - highlight all of the legal moves from the square\s
                    """;
        } else {
            return """
                    leave - leave the game\s
                    redraw - redraws the board\s
                    highlight <ROW> <COL> - highlight all of the legal moves from the square\s
                    """;
        }
    }


    private String register(String[] command) throws ResponseException, IOException {
        JsonObject response;
        verifyArguments(command, 4,
                "register <USERNAME> <PASSWORD> <EMAIL> \n");

        response = facade.register(command[1], command[2], command[3]);
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        if (jsonObject.get("success").getAsBoolean()) {
            authToken = jsonObject.get("authToken").getAsString();
            loggedIn = true;
            return "Logged in as " + command[1];
        } else {
            return "Failed to register " + command[1];
        }
    }

    private String login(String[] command) throws ResponseException, IOException {
        JsonObject response;
        verifyArguments(command, 3,
                "login <USERNAME> <PASSWORD> \n");

        response = facade.login(command[1], command[2]);
        if (response.get("success").getAsBoolean()) {
            authToken = response.get("authToken").getAsString();
            loggedIn = true;
            return "Logged in as " + command[1];
        }
        else {
            return "Failed to login " + command[1];
        }
    }

    private String logout(String[] command) throws IOException {
        JsonObject response = facade.logout(authToken);
        if (response.get("success").getAsBoolean()) {
            authToken = "";
            loggedIn = false;
            return "Logged out successfully";
        } else {
            return "Failed to log out";
        }
    }

    private String create(String[] command) throws IOException, ResponseException {
        verifyArguments(command, 2, "create <NAME> \n");
        JsonObject response = facade.createGame(command[1], authToken);
        if (response.get("success").getAsBoolean()) {
            return "Created game " + command[1];
        } else {
            return "Failed to create game " + command[1];
        }
    }

    private String list(String[] command) throws ResponseException, IOException {
        verifyArguments(command, 1, "list");
        JsonObject response = facade.listGames(authToken);
        StringBuilder gameList = new StringBuilder("List of games: \n");
        if (response.get("success").getAsBoolean()) {
            JsonArray gameArray = response.get("games").getAsJsonArray();
            for (int i = 1; i < gameArray.size() + 1; i++) {
                gameMap.put(i, gameArray.get(i - 1));
            }
            if (gameMap.isEmpty()) {
                return "No games";
            }
            for (Map.Entry<Integer, JsonElement> entry : gameMap.entrySet()) {
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
                gameList.append("Game number: ").append(entry.getKey())
                        .append(", game name: ").append(game.get("gameName").getAsString())
                        .append(", white username: ").append(whiteUsername)
                        .append(", black username: ").append(blackUsername)
                        .append("\n");
            }

        }
        return gameList.toString();
    }

    private String join(String[] command) throws ResponseException, IOException {
        int gameNumber;
        String gameIDString;
        String color = command[2];

        if (gameMap.isEmpty()) {
            return list(command);
        }

        verifyArguments(command, 3,
                "join <ID> [WHITE|BLACK] \n");
        try {
            gameNumber = Integer.parseInt(command[1]);
        } catch (Exception e) {
            return "Could not parse game ID";
        }

        if (gameNumber > gameMap.size()) {
            return "Game does not exist";
        }

        color = color.toUpperCase();

        if (!color.equals("WHITE") && !color.equals("BLACK")) {
            return "Invalid color";
        }

        gameID = gameMap.get(gameNumber).getAsJsonObject().get("gameID").getAsInt();
        gameIDString = String.valueOf(gameID);

        JsonObject response = facade.joinGame(color, gameIDString, authToken);
        if (response.get("success").getAsBoolean()) {
            wsFacade = new WebSocketFacade(baseUrl, serverMessageHandler);
            wsFacade.connect(color, authToken, gameID);
            inGameplay = true;
            player = true;
            return "Joined game with ID " + command[1] + " as color " + command[2];
        } else {
            return "Failed to join game";
        }
    }

    private String observe(String[] command) throws ResponseException, IOException {
        int gameNumber;

        if (gameMap.isEmpty()) {
            return list(command);
        }

        verifyArguments(command, 2,
                "observe <ID> \n");
        try {
            gameNumber = Integer.parseInt(command[1]);
        } catch (Exception e) {
            return "Could not parse game ID";
        }
        if (gameNumber > gameMap.size()) {
            return "Game does not exist";
        }
        gameID = gameMap.get(gameNumber).getAsJsonObject().get("gameID").getAsInt();
        wsFacade = new WebSocketFacade(baseUrl, serverMessageHandler);
        wsFacade.connect(null, authToken, gameID);
        inGameplay = true;
        return "";
    }

    private String move(String[] command) throws IOException {
        wsFacade.makeMove(Integer.parseInt(command[1]), command[2], Integer.parseInt(command[3]),
                command[4], command[5],
                authToken, gameID);
        return "";
    }

    private String leave(String[] command) {
        inGameplay = false;
        return "";
    }

    private String resign(String[] command) {
        return "";
    }

    private String redraw(String[] command) {
        return "";
    }

    private String highlight(String[] command) {
        return "";
    }



    private void drawBoard() {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        out.print(ERASE_SCREEN);
        drawChessBoard(out, board, false);
        drawChessBoard(out, board, true);
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }




    private void verifyArguments(String[] command, int correctAmount, String message) throws ResponseException {
        if (command.length != correctAmount) {
            throw new ResponseException(400, "Wrong argument length, structure is\n" + message);
        }
    }

}
