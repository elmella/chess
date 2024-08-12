package service;

import chess.*;
import dataaccess.*;
import model.GameData;
import request.CreateGameRequest;
import request.CreateGameResponse;
import request.JoinGameRequest;
import result.GameResponse;
import result.ListGamesResponse;
import result.Response;
import websocket.commands.*;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class GameService extends Service {
    private final GameDAOInterface gameDAO;

    public GameService(GameDAOInterface gameDAO) {
        this.gameDAO = gameDAO;
    }

    public ListGamesResponse getGames() throws DataAccessException {
        ArrayList<GameData> games = gameDAO.listGames();
        ArrayList<GameResponse> responses = new ArrayList<>();
        for (GameData game : games) {

            // Turn game data into game response
            responses.add(new GameResponse(game.getGameID(), game.getWhiteUsername(),
                    game.getBlackUsername(), game.getGameName()));
        }
        return new ListGamesResponse(responses, true, null);
    }

    public CreateGameResponse createGame(CreateGameRequest request) throws DataAccessException, BadRequestException {
        hasNullFields(request);
        String gameName = request.gameName();
        int gameID = createGameID();
        GameData game = new GameData(gameID, null, null, gameName, new ChessGame());
        gameDAO.createGame(game);

        return new CreateGameResponse(gameID, true, null);
    }

    public Response joinGame(JoinGameRequest request, String username)
            throws AlreadyTakenException, BadRequestException, DataAccessException {
        hasNullFields(request);
        int gameID = request.gameID();
        String color = request.playerColor();
        GameData currentGame;
        String whiteUsername = null;
        String blackUsername = null;
        String gameName;
        ChessGame game;

        // Get current game before updating it
        currentGame = gameDAO.getGame(gameID);
        if (currentGame.getGameName() == null) {
            throw new BadRequestException("Error: bad request");
        }
        gameName = currentGame.getGameName();
        game = currentGame.getGame();

        // Add the username to the correct color
        if (color.equals("WHITE")) {

            // Verify color is available
            if (currentGame.getWhiteUsername() != null && !currentGame.getWhiteUsername().equals(username)) {
                throw new AlreadyTakenException("Error: already taken");
            }
            whiteUsername = username;
            blackUsername = currentGame.getBlackUsername();
        } else if (color.equals("BLACK")) {

            // Verify color is available
            if (currentGame.getBlackUsername() != null && !currentGame.getBlackUsername().equals(username)) {
                throw new AlreadyTakenException("Error: already taken");
            }
            blackUsername = username;
            whiteUsername = currentGame.getWhiteUsername();
        }

        // Update the game
        GameData updatedGame = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        gameDAO.updateGame(updatedGame);

        return new Response(null, true);
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return gameDAO.getGame(gameID);
    }

    public LoadGameMessage loadGame(UserGameCommand command) throws DataAccessException {
        GameData gameData = gameDAO.getGame(command.getGameID());
        ChessGame.TeamColor color = getTeamColor(command);
        return new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData.getGame(),
                null, null, null, null, color);
    }

    public void leaveGame(LeaveGameCommand command) throws DataAccessException {
        ChessGame.TeamColor leaveColor = getTeamColor(command);
        if (leaveColor == null) {
            return;
        }
        GameData gameData = gameDAO.getGame(command.getGameID());
        if (leaveColor.equals(WHITE)) {
            gameData.setWhiteUsername(null);
        } else {
            gameData.setBlackUsername(null);
        }
        gameDAO.updateGame(gameData);
    }

    public void resign(ResignCommand command) throws DataAccessException, GameOverException, NotPlayerException {
        ChessGame.TeamColor color = getTeamColor(command);
        if (color == null) {
            throw new NotPlayerException("Error: must be player to resign");
        }
        GameData gameData = gameDAO.getGame(command.getGameID());
        ChessGame game = gameData.getGame();
        if (game.isGameOver()) {
            throw new GameOverException("Error, game is over");
        }
        gameData.getGame().setGameOver(true);
        gameDAO.updateGame(gameData);
    }

    public LoadGameMessage makeMove(MakeMoveCommand makeMoveCommand)
            throws DataAccessException, InvalidMoveException, GameOverException {

        Map<Integer, String> alphaIntMap = Map.of(1, "a", 2, "b", 3, "c", 4,
                "d", 5, "e", 6, "f", 7, "g", 8, "h");

        // Get chess data from database
        GameData gameData = gameDAO.getGame(makeMoveCommand.getGameID());
        ChessMove move = makeMoveCommand.getMove();
        ChessGame game = gameData.getGame();
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();
        ChessPiece piece = game.getBoard().getPiece(startPos);
        ChessGame.TeamColor teamColor = piece.getTeamColor();

        ChessGame.TeamColor clientColor = getTeamColor(makeMoveCommand);

        // Verify requester is correct color
        if (!teamColor.equals(clientColor)) {
            if (clientColor == null) {
                throw new InvalidMoveException("Error: you are not a player");
            }
            throw new InvalidMoveException("Error: not your turn");
        }

        // Verify game is not over
        if (game.isGameOver()) {
            throw new GameOverException("Error: game is over");
        }


        // Piece together move notification
        String startPosString = startPos.getRow() + " " + alphaIntMap.get(startPos.getColumn());
        String endPosString = endPos.getRow() + " " + alphaIntMap.get(endPos.getColumn());

        String moveNotification = teamColor + " moves " + piece.getPieceType()
                + " from " + startPosString + " to " + endPosString;
        if (move.getPromotionPiece() != null) {
            moveNotification += ", promoting it to a " + move.getPromotionPiece();
        }

        // Make move and add it to database
        game.makeMove(move);
        gameDAO.updateGame(gameData);

        // Create game status notifications
        String checkNotification = null;
        String checkmateNotification = null;
        String stalemateNotification = null;
        ChessGame.TeamColor newTeam = game.getTeamTurn();
        if (game.isInCheck(newTeam)) {
            checkNotification = newTeam + " is in check";
        }
        if (game.isInCheckmate(newTeam)) {
            checkmateNotification = newTeam + " is in checkmate";

            // Avoid redundant notification for check if in checkmate
            checkNotification = null;
        }
        if (game.isInStalemate(newTeam)) {
            stalemateNotification = newTeam + " is in stalemate";
        }
        return new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData.getGame(),
                checkNotification, checkmateNotification, stalemateNotification, moveNotification, clientColor);
    }

    private int createGameID() throws DataAccessException {
        Random r = new Random();
        int gameID = r.nextInt(8999) + 1000;
        while ((gameDAO.getGame(gameID).getGameName() != null)) {
            gameID = r.nextInt(8999) + 1000;
        }
        return gameID;
    }

    public ChessGame.TeamColor getTeamColor(UserGameCommand command) throws DataAccessException {
        GameData gameData = getGame(command.getGameID());
        AuthService auth = new AuthService(AuthDAO.getInstance());
        String username = auth.getUsername(command.getAuthToken());
        String whiteUsername = gameData.getWhiteUsername();
        String blackUsername = gameData.getBlackUsername();
        if (whiteUsername != null && whiteUsername.equals(username)) {
            return WHITE;
        } else if (blackUsername != null && blackUsername.equals(username)) {
            return BLACK;
        } return null;
    }

    public ChessGame.TeamColor bystanderColor(int gameID, String username) throws DataAccessException {
        GameData gameData = getGame(gameID);
        if (gameData.getWhiteUsername().equals(username)) {
            return WHITE;
        } else if (gameData.getBlackUsername().equals(username)) {
            return BLACK;
        } return null;
    }
}
