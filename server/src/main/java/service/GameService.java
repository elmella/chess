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
import websocket.commands.LeaveGameCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.ResignCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

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
            responses.add(new GameResponse(game.getGameID(), game.getWhiteUsername(), game.getBlackUsername(), game.getGameName()));
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

    public Response joinGame(JoinGameRequest request, String username) throws AlreadyTakenException, BadRequestException, DataAccessException {
        hasNullFields(request);
        int gameID = request.gameID();
        String color = request.playerColor();
        GameData currentGame;
        String whiteUsername;
        String blackUsername;
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
            if (currentGame.getWhiteUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            }
            whiteUsername = username;
            blackUsername = currentGame.getBlackUsername();
        } else {

            // Verify color is available
            if (currentGame.getBlackUsername() != null) {
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
        return new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData.getGame(), null, null, null, null);
    }

    public void leaveGame(LeaveGameCommand command, ChessGame.TeamColor leaveColor) throws DataAccessException {
        GameData gameData = gameDAO.getGame(command.getGameID());
        if (leaveColor.equals(ChessGame.TeamColor.WHITE)) {
            gameData.setWhiteUsername(null);
        } else {
            gameData.setBlackUsername(null);
        }
        gameDAO.updateGame(gameData);
    }

    public void resign(ResignCommand command) throws DataAccessException, GameOverException {
        GameData gameData = gameDAO.getGame(command.getGameID());
        ChessGame game = gameData.getGame();
        if (game.isGameOver()) {
            throw new GameOverException("Error, game is over");
        }
        gameData.getGame().setGameOver(true);
        gameDAO.updateGame(gameData);
    }

    public LoadGameMessage makeMove(MakeMoveCommand makeMoveCommand, ChessGame.TeamColor clientColor) throws DataAccessException, InvalidMoveException, GameOverException {

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

        // Verify requester is correct color
        if (!teamColor.equals(clientColor)) {
            throw new InvalidMoveException("Error: not correct color");
        }

        // Verify game is not over
        if (game.isGameOver()) {
            throw new GameOverException("Error: game is over");
        }


        // Piece together move notification
        String startPosString = startPos.getRow() + " " + alphaIntMap.get(startPos.getColumn());
        String endPosString = endPos.getRow() + " " + alphaIntMap.get(endPos.getColumn());

        String moveNotification = teamColor + " moves " + piece.getPieceType() + "from " + startPosString + " to " + endPosString;
        if (move.getPromotionPiece() != null) {
            moveNotification += ", promoting it to " + move.getPromotionPiece();
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
        return new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData.getGame(), checkNotification, checkmateNotification, stalemateNotification, moveNotification);
    }

    private int createGameID() throws DataAccessException {
        Random r = new Random();
        int gameID = r.nextInt(8999) + 1000;
        while ((gameDAO.getGame(gameID).getGameName() != null)) {
            gameID = r.nextInt(8999) + 1000;
        }
        return gameID;
    }


}
