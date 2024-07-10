package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAOInterface;
import model.GameData;
import request.CreateGameRequest;
import request.CreateGameResponse;
import result.GameResponse;
import result.ListGamesResponse;

import java.util.ArrayList;
import java.util.Random;

public class GameService {
//    private final UserDAOInterface userDAO;
//    private final AuthDAOInterface authDAO;
    private final GameDAOInterface gameDAO;

//    public GameService(UserDAOInterface userDAO, AuthDAOInterface authDAO, GameDAOInterface gameDAO) {
public GameService(GameDAOInterface gameDAO) {
//        this.userDAO = userDAO;
//        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }


    public ListGamesResponse getGames() {
        try {
            ArrayList<GameData> games = gameDAO.listGames();
            ArrayList<GameResponse> responses = new ArrayList<>();
            for (GameData game : games) {
                responses.add(new GameResponse(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
            }
            return new ListGamesResponse(responses, true, null);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public CreateGameResponse createGame(CreateGameRequest request) {
    String gameName = request.gameName();
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
    GameData game = new GameData(gameID, null, null, gameName, new ChessGame());
        try {
            gameDAO.createGame(game);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        return new CreateGameResponse(gameID, true, null);

    }



}
