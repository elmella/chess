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

import java.util.ArrayList;
import java.util.Random;

public class GameService {
    //    private final UserDAOInterface userDAO;
    private final AuthDAOInterface authDAO;
    private final GameDAOInterface gameDAO;

    //    public GameService(UserDAOInterface userDAO, AuthDAOInterface authDAO, GameDAOInterface gameDAO) {
    public GameService(GameDAOInterface gameDAO, AuthDAOInterface authDAO) {
//        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    private int createGameID() throws DataAccessException {
        Random r = new Random();
        int gameID = r.nextInt(8999) + 1000;
        while ((gameDAO.getGame(gameID) != null)) {
            gameID = r.nextInt(8999) + 1000;
        }
        return gameID;
    }


    public ListGamesResponse getGames() throws DataAccessException {
        ArrayList<GameData> games = gameDAO.listGames();
        ArrayList<GameResponse> responses = new ArrayList<>();
        for (GameData game : games) {
            responses.add(new GameResponse(game.getGameID(), game.getWhiteUsername(), game.getBlackUsername(), game.getGameName()));
        }
        return new ListGamesResponse(responses, true, null);
    }

    public CreateGameResponse createGame(CreateGameRequest request) throws DataAccessException {
        String gameName = request.gameName();
        int gameID = createGameID();
        GameData game = new GameData(gameID, null, null, gameName, new ChessGame());
        gameDAO.createGame(game);

        return new CreateGameResponse(gameID, true, null);
    }

    public Response joinGame(JoinGameRequest request, String authToken) throws AlreadyTakenException, BadRequestException, DataAccessException {
        int gameID = request.gameID();
        String color = request.playerColor();
        GameData currentGame;
        String username;
        String whiteUsername;
        String blackUsername;
        String gameName;
        ChessGame game;
        // Get username
        AuthData auth = authDAO.getAuth(authToken);
        username = auth.username();

        // Get current game before updating it
        currentGame = gameDAO.getGame(gameID);
        if (currentGame == null) {
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

}
