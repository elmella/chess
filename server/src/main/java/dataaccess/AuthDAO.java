package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.SQLException;

public class AuthDAO implements AuthDAOInterface {

    @Override
    public void createAuth(AuthData a) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO auth " +
                    "(authToken, username) VALUES(?, ?)")) {
                preparedStatement.setString(1, a.authToken());
                preparedStatement.setString(2, a.username());
                var rs = preparedStatement.executeQuery();
                rs.next();
                System.out.println(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: issue communicating with database");
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        String foundAuthToken = null;
        String foundUsername = null;
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM auth a" +
                    "WHERE a.authToken = ?")) {
                preparedStatement.setString(1, authToken);
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        foundAuthToken = rs.getString("authToken");
                        foundUsername = rs.getString("username");
                    }
                    return new AuthData(foundAuthToken, foundUsername);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: issue communicating with database");
        }
    }

    @Override
    public void deleteAuth(AuthData a) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM auth a " +
                    "WHERE (a.authToken = ? AND a.username = ?)")) {
                preparedStatement.setString(1, a.authToken());
                preparedStatement.setString(2, a.username());
                var rs = preparedStatement.executeQuery();
                rs.next();
                System.out.println(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: issue communicating with database");
        }
    }

    @Override
    public void clearAuth() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM auth")) {
                var rs = preparedStatement.executeQuery();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: issue communicating with database");
        }
    }
}
