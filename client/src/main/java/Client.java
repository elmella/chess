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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.out;
import static ui.DrawBoard.drawChessBoard;
import static ui.EscapeSequences.*;

public class Client {
    private final ServerFacade FACADE;
    private WebSocketFacade WS_FACADE;
    private final String BASE_URL;
    private final ServerMessageHandler serverMessageHandler;
    private String authToken;
    private boolean loggedIn;
    private final Gson GSON = new Gson();
    private static final HashMap<Integer, JsonElement> GAME_MAP = new HashMap<>();

    public Client(String BASE_URL, ServerMessageHandler serverMessageHandler) {
        this.BASE_URL = BASE_URL;
        this.serverMessageHandler = serverMessageHandler;
        FACADE = new ServerFacade(BASE_URL);
        loggedIn = false;
    }

    public String loggedOutMenu(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String baseCommand = (tokens.length > 0) ? tokens[0] : "help";
            String[] command = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (baseCommand) {
                case "login" -> login(command);
                case "register" -> register(command);
                case "quit" -> "quit";
                default -> loggedOutHelp();
            };
        } catch (ResponseException | IOException e) {
            return e.getMessage();
        }
    }





    public String gamePlayMenu(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String baseCommand = (tokens.length > 0) ? tokens[0] : "help";
            String[] command = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (baseCommand) {
                case "create" -> create(command);
                case "list" -> list(command);
                case "join" -> join(command);
                case "observe" -> observe(command);
                case "logout" -> logout(command);
                case "quit" -> "quit";
                default -> loggedInHelp();
            };
        } catch (ResponseException | IOException e) {
            return e.getMessage();
        }
    }

    public String loggedOutHelp() {
            return "register <USERNAME> <PASSWORD> <EMAIL> - to create an account %n" +
                    "login <USERNAME> <PASSWORD> - to play chess %n" + "quit - playing chess %n" +
                    "help - with possible commands %n";

    }

    public String loggedInHelp() {
        return "create <NAME> - a game %n" +
                "list - games %n" +
                "join <ID> [WHITE|BLACK] - a game %n" +
                "observe <ID> - a game %n" +
                "logout - when you are done %n" +
                "quit - playing chess %n" +
                "help - with possible commands %n";
    }

    public String gamePlayHelp() {
        return "move <START_ROW> <START_COL> <END_ROW> <END_COL> - make move (move 2 a 4 a)" +
                "leave - leave the game" +
                "resign - end the game and lose" +
                "redraw - redraws the board" +
                "highlight <ROW> <COL> - highlight all of the legal moves from the square";
    }


    private String register(String[] command) throws ResponseException, IOException {
        JsonObject response;
        verifyArguments(command, 4,
                "register <USERNAME> <PASSWORD> <EMAIL>");

        response = FACADE.register(command[1], command[2], command[3]);
        JsonObject jsonObject = GSON.fromJson(response, JsonObject.class);
        if (jsonObject.get("success").getAsBoolean()) {
            authToken = jsonObject.get("authToken").getAsString();
            out.println("Logged in as " + command[1]);
            loggedIn = true;
        }

        return response.getAsString();
    }

    private String login(String[] command) throws ResponseException, IOException {
        JsonObject response;
        verifyArguments(command, 3,
                "login <USERNAME> <PASSWORD>");

        response = FACADE.login(command[1], command[2]);
        if (response.get("success").getAsBoolean()) {
            authToken = response.get("authToken").getAsString();
            out.println("Logged in as " + command[1]);
            loggedIn = true;
        }
        return response.getAsString();
    }

    private String logout(String[] command) throws IOException {
        JsonObject response = FACADE.logout(authToken);
        if (response.get("success").getAsBoolean()) {
            out.println("Logged out");
            authToken = "";
            loggedIn = false;
        }
        return response.getAsString();
    }

    private String create(String[] command) throws IOException, ResponseException {
        verifyArguments(command, 2, "create <NAME>");
        JsonObject response = FACADE.createGame(command[1], authToken);
        if (response.get("success").getAsBoolean()) {
            out.println("Created game " + command[1]);
        }
        return response.getAsString();
    }

    private String list(String[] command) throws ResponseException, IOException {
        verifyArguments(command, 1, "list");
        JsonObject response = FACADE.listGames(authToken);
        StringBuilder gameList = new StringBuilder("List of games: \n");
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

        if (GAME_MAP.isEmpty()) {
            return list(command);
        }

        verifyArguments(command, 3,
                "join <ID> [WHITE|BLACK]");
        try {
            gameNumber = Integer.parseInt(command[1]);
        } catch (Exception e) {
            return "Could not parse game ID";
        }

        if (gameNumber > GAME_MAP.size()) {
            return "Game does not exist";
        }

        if (!command[2].equals("WHITE") && !command[2].equals("BLACK")) {
            return "Invalid color";
        }

        gameIDString = GAME_MAP.get(gameNumber).getAsJsonObject().get("gameID").getAsString();

        JsonObject response = FACADE.joinGame(command[2], gameIDString, authToken);
        WS_FACADE = new WebSocketFacade(BASE_URL, serverMessageHandler);
        if (response.get("success").getAsBoolean()) {
            out.println("Joined game with ID " + command[1] + " as color " + command[2]);
        } else {
            out.println("Failed to join game");
        }

        return response.getAsString();
    }

    private String observe(String[] command) throws ResponseException, IOException {
        int gameNumber;

        if (GAME_MAP.isEmpty()) {
            return list(command);
        }

        verifyArguments(command, 3,
                "observe <ID>");
        try {
            gameNumber = Integer.parseInt(command[1]);
        } catch (Exception e) {
            return "Could not parse game ID";
        }
        if (gameNumber > GAME_MAP.size()) {
            return "Game does not exist";
        }
        WS_FACADE = new WebSocketFacade(BASE_URL, serverMessageHandler);

        drawBoard();
        return "Chess board";
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
            throw new ResponseException(400, message);
        }

    }

}
