package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAO implements UserDAOInterface {
    private static UserDAO instance;


    public UserDAO() {
    }

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public void createUser(UserData u) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO user " +
                    "(username, password, email) VALUES(?, ?, ?)")) {
                preparedStatement.setString(1, u.username());
                preparedStatement.setString(2, hashPassword(u.password()));
                preparedStatement.setString(3, u.email());
                var rs = preparedStatement.executeQuery();
                rs.next();
                System.out.println(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: issue communicating with database");
        }
    }

    @Override
    public UserData getUser(String username, String password) throws DataAccessException {
        String foundUsername = null;
        String foundPassword = null;
        String foundEmail = null;
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM user u" +
                    "WHERE u.username = ? AND u.password = ?")) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, hashPassword(password));
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        foundUsername = rs.getString("username");
                        foundPassword = rs.getString("password");
                        foundEmail = rs.getString("email");
                    }
                    return new UserData(foundUsername, foundPassword, foundEmail);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: issue communicating with database");
        }
    }

    @Override
    public void clearUser() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM user")) {
                var rs = preparedStatement.executeQuery();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: issue communicating with database");
        }
    }
}