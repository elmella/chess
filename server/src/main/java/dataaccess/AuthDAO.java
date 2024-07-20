package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class AuthDAO implements AuthDAOInterface {
    private static AuthDAO instance;

    public AuthDAO() {
    }

    public static AuthDAO getInstance() {
        if (instance == null) {
            instance = new AuthDAO();
        }
        return instance;
    }


    @Override
    public void createAuth(AuthData a) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO auth " +
                    "(authToken, username) VALUES(?, ?)")) {
                preparedStatement.setString(1, a.authToken());
                preparedStatement.setString(2, a.username());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.toString());
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        String foundAuthToken = null;
        String foundUsername = null;
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM auth a " +
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
            throw new DataAccessException(e.toString());
        }
    }

    @Override
    public void deleteAuth(AuthData a) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM auth a " +
                    "WHERE (a.authToken = ? AND a.username = ?)")) {
                preparedStatement.setString(1, a.authToken());
                preparedStatement.setString(2, a.username());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.toString());
        }
    }

    @Override
    public void clearAuth() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM auth")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.toString());
        }
    }
}
