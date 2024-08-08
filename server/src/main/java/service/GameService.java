package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import request.CreateGameRequest;
import request.CreateGameResponse;
import request.JoinGameRequest;
import result.GameResponse;
import result.ListGamesResponse;
import result.Response;
import websocket.commands.ConnectCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import java.util.ArrayList;
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

    public LoadGameMessage loadGame(UserGameCommand command) throws DataAccessException {
        GameData gameData = gameDAO.getGame(command.getGameID());
        return new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData.getGame());
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
