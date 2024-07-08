package dataaccess;

import model.GameData;
import model.UserData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAOInterface {
    ArrayList<GameData> game;

    public MemoryGameDAO() {
        game = new ArrayList<>();
    }

    @Override
    public void createGame(GameData g) throws DataAccessException {
        if (game.contains(g)) {
            throw new DataAccessException("Auth already exists");
        }
        game.add(g);
    }

    @Override
    public GameData getGame(int gameID, String gameName) throws DataAccessException {
        for (GameData g : game) {
            if (g.gameID() == gameID && g.gameName().equals(gameName)) {
                return g;
            }
        }
        throw new DataAccessException("Game ID or Game Name not found");
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        return game;
    }

    @Override
    public void updateGame(GameData g) throws DataAccessException {
        if (!game.contains(g)) {
            throw new DataAccessException("User not found");
        }
        game.remove(g);
        game.add(g);
    }

    @Override
    public void deleteGame(GameData g) throws DataAccessException {
        if (!game.contains(g)) {
            throw new DataAccessException("User not found");
        }
        game.remove(g);
    }
}