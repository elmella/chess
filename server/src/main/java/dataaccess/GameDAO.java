package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import handler.UseGson;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;

public class GameDAO implements GameDAOInterface {
    private static GameDAO instance;


    public GameDAO() {
    }

    public static GameDAO getInstance() {
        if (instance == null) {
            instance = new GameDAO();
        }
        return instance;
    }

    @Override
    public void createGame(GameData g) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO game " +
                    "(gameID, whiteUsername, blackUsername, gameName, game) " +
                    "VALUES(?, ?, ?, ?, ?)")) {
                preparedStatement.setString(1, String.valueOf(g.getGameID()));
                preparedStatement.setString(2, g.getWhiteUsername());
                preparedStatement.setString(3, g.getBlackUsername());
                preparedStatement.setString(4, g.getGameName());
                preparedStatement.setString(5, UseGson.toJson(g.getGame()));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.toString());
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        int foundGameID = 0;
        String foundWhiteUsername = null;
        String foundBlackUsername = null;
        String foundGameName = null;
        ChessGame foundGame = null;
        Gson gson = new Gson();

        try (var conn = DatabaseManager.getConnection()) {

            try (var preparedStatement = conn.prepareStatement("SELECT * FROM game g " + "WHERE g.gameID = ?")) {
                preparedStatement.setString(1, String.valueOf(gameID));
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        foundGameID = rs.getInt("gameID");
                        foundWhiteUsername = rs.getString("whiteUsername");
                        foundBlackUsername = rs.getString("blackUsername");
                        foundGameName = rs.getString("gameName");
                        foundGame = gson.fromJson(rs.getString("game"), ChessGame.class);
                    }
                    return new GameData(foundGameID, foundWhiteUsername, foundBlackUsername, foundGameName, foundGame);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.toString());
        }
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> games = new ArrayList<>();
        Gson gson = new Gson();

        try (var conn = DatabaseManager.getConnection()) {

            try (var preparedStatement = conn.prepareStatement("SELECT * FROM game")) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        int foundGameID = rs.getInt("gameID");
                        String foundWhiteUsername = rs.getString("whiteUsername");
                        String foundBlackUsername = rs.getString("blackUsername");
                        String foundGameName = rs.getString("gameName");
                        ChessGame foundGame = gson.fromJson(rs.getString("game"), ChessGame.class);
                        games.add(new GameData(foundGameID, foundWhiteUsername, foundBlackUsername, foundGameName, foundGame));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.toString());
        }
        return games;
    }

    @Override
    public void updateGame(GameData g) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("UPDATE game " +
                    "SET whiteUsername = ?, blackUsername = ?," +
                    " gameName = ?, game = ? WHERE gameID = ?")) {
                preparedStatement.setString(1, g.getWhiteUsername());
                preparedStatement.setString(2, g.getBlackUsername());
                preparedStatement.setString(3, g.getGameName());
                preparedStatement.setString(4, UseGson.toJson(g.getGame()));
                preparedStatement.setString(5, String.valueOf(g.getGameID()));

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.toString());
        }
    }

    @Override
    public void clearGame() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM game")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.toString());
        }

    }
}
