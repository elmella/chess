package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAOInterface {

    void createGame(GameData g) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    ArrayList<GameData> listGames() throws DataAccessException;

    void updateGame(GameData g) throws DataAccessException;

    void deleteGame(GameData g) throws DataAccessException;

    void clearGame() throws DataAccessException;

    boolean isEmpty() throws DataAccessException;
}