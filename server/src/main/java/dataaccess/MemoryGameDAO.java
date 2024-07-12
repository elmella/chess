package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAOInterface {
    private static MemoryGameDAO instance;
    ArrayList<GameData> game;

    private MemoryGameDAO() {
        game = new ArrayList<>();
    }

    public static MemoryGameDAO getInstance() {
        if (instance == null) {
            instance = new MemoryGameDAO();
        }
        return instance;
    }

    @Override
    public void createGame(GameData g) throws DataAccessException {
        game.add(g);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        for (GameData g : game) {
            if (g.getGameID() == gameID) {
                return g;
            }
        }
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        return game;
    }

    @Override
    public void updateGame(GameData g) throws DataAccessException {
        GameData currentGame = getGame(g.getGameID());
        currentGame.setGameID(g.getGameID());
        currentGame.setGameName(g.getGameName());
        currentGame.setBlackUsername(g.getBlackUsername());
        currentGame.setWhiteUsername(g.getWhiteUsername());
        currentGame.setGame(g.getGame());
    }


    @Override
    public void clearGame() throws DataAccessException {
        game = new ArrayList<>();
    }

}