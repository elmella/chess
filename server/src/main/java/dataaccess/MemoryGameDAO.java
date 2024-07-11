package dataaccess;

import model.GameData;
import model.UserData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAOInterface {
    ArrayList<GameData> game;
    private static MemoryGameDAO instance;

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
//        if (!game.contains(g)) {
//            throw new DataAccessException("User not found");
//        }
        GameData currentGame = getGame(g.getGameID());

        currentGame.setGameID(g.getGameID());
        currentGame.setGameName(g.getGameName());
        currentGame.setBlackUsername(g.getBlackUsername());
        currentGame.setWhiteUsername(g.getWhiteUsername());
        currentGame.setGame(g.getGame());
//        game.remove(g);
//        game.add(g);
    }

    @Override
    public void deleteGame(GameData g) throws DataAccessException {
        if (!game.contains(g)) {
            throw new DataAccessException("User not found");
        }
        game.remove(g);
    }

    @Override
    public void clearGame() throws DataAccessException {
        game = new ArrayList<>();
    }

    @Override
    public boolean isEmpty() throws DataAccessException {
        return game.isEmpty();
    }
}