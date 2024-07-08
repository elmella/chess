package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAOInterface {
    ArrayList<GameData> game;

    public MemoryGameDAO() {
        game = new ArrayList<>();
    }

    @Override
    public void createGame(GameData g) throws DataAccessException {

    }

    @Override
    public GameData getGame(int gameID, String gameName) throws DataAccessException {
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData g) throws DataAccessException {

    }

    @Override
    public void deleteGame(GameData g) throws DataAccessException {

    }
}