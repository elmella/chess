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

    private int createGameID() {
        Random r = new Random();
        int gameID = r.nextInt(8999) + 1000;
        while (true) {
            try {
                if (gameDAO.getGame(gameID) == null) break;
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
            gameID = r.nextInt(8999) + 1000;
        }
        return gameID;
    }


    public ListGamesResponse getGames() {
        try {
            ArrayList<GameData> games = gameDAO.listGames();
            ArrayList<GameResponse> responses = new ArrayList<>();
            for (GameData game : games) {
                responses.add(new GameResponse(game.getGameID(), game.getWhiteUsername(), game.getBlackUsername(), game.getGameName()));
            }
            return new ListGamesResponse(responses, true, null);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public CreateGameResponse createGame(CreateGameRequest request) {
        String gameName = request.gameName();
        int gameID = createGameID();
        GameData game = new GameData(gameID, null, null, gameName, new ChessGame());
        try {
            gameDAO.createGame(game);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        return new CreateGameResponse(gameID, true, null);
    }

    public Response joinGame(JoinGameRequest request, String authToken) throws GameAlreadyTakenException, BadRequestException {
        int gameID = request.gameID();
        String color = request.playerColor();
        GameData currentGame;
        String username;
        String whiteUsername;
        String blackUsername;
        String gameName;
        ChessGame game;
        try {
            AuthData auth = authDAO.getAuth(authToken);
            username = auth.username();
            currentGame = gameDAO.getGame(gameID);
            if (currentGame == null) {
                throw new BadRequestException("Error: bad request");
            }
            gameName = currentGame.getGameName();
            game = currentGame.getGame();
            if (color.equals("WHITE")) {
                if (currentGame.getWhiteUsername() != null) {
                    throw new GameAlreadyTakenException("Error: already taken");
                }
                whiteUsername = username;
                blackUsername = currentGame.getBlackUsername();
            } else {
                if (currentGame.getBlackUsername() != null) {
                    throw new GameAlreadyTakenException("Error: already taken");
                }
                blackUsername = username;
                whiteUsername = currentGame.getWhiteUsername();
            }
            GameData updatedGame = new GameData(gameID, whiteUsername, blackUsername, gameName, game);

            gameDAO.updateGame(updatedGame);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return new Response(null, true);
    }


}
