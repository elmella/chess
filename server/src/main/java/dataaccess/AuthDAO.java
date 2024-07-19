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
        String foundUsername = null;
        String foundPassword = null;
        String email = null;
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM user u" +
                    "WHERE u.username = ? AND u.password = ?")) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, hashPassword(password));
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        foundUsername = rs.getString("username");
                        foundPassword = rs.getString("password");
                        email = rs.getString("email");
                    }
                    return new UserData(foundUsername, foundPassword, email);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: issue communicating with database");
        }
    }

    @Override
    public void deleteAuth(AuthData a) throws DataAccessException {

    }

    @Override
    public void clearAuth() throws DataAccessException {

    }
}
