package dataaccess;

import chess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import service.ClearService;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DAOTests {

    private final AuthDAO authDAO = new AuthDAO();
    private final UserDAO userDAO = new UserDAO();
    private final GameDAO gameDAO = new GameDAO();
    private final ClearService clear = new ClearService(AuthDAO.getInstance(), GameDAO.getInstance(), UserDAO.getInstance());

    private final String username = "emily";
    private final String password = "iambeautiful";
    private final String email = "email@email.com";
    private final int gameID = 1;
    private final String gameName = "game";
    private final ChessGame game = new ChessGame();
    private final String authToken = "let me in";
    private final int gameID2 = 2;
    private final String gameName2 = "game2";
    private final ChessGame game2 = new ChessGame();
    private final GameData gameData2 = new GameData(gameID2, null, null, gameName2, game2);
    private UserData userData = new UserData(username, password, email);
    private GameData gameData = new GameData(gameID, null, null, gameName, game);
    private AuthData authData = new AuthData(authToken, username);

    @BeforeEach
    public void clear() throws DataAccessException {
        clear.clear();
        userData = new UserData(username, password, email);
        gameData = new GameData(gameID, null, null, gameName, game);
        authData = new AuthData(authToken, username);

    }

    @Test
    @Order(1)
    @DisplayName("Create User Success")
    public void createUserSuccess() {
        // Create user, make sure no errors are thrown
        Assertions.assertDoesNotThrow(() -> userDAO.createUser(userData));
    }

    @Test
    @Order(2)
    @DisplayName("Create User Failure")
    public void createUserFailure() {
        try {
            // Create user
            userDAO.createUser(userData);

            // Try creating user again, should fail primary key condition
            Assertions.assertThrows(DataAccessException.class, () -> userDAO.createUser(userData));

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(3)
    @DisplayName("Get User Success")
    public void getUserSuccess() {
        try {
            // Create user
            userDAO.createUser(userData);

            // Get user, verify usernames are equal
            Assertions.assertEquals(username, userDAO.getUser(username, password).username());

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(4)
    @DisplayName("Get User Failure")
    public void getUserFailure() {

        try {
            // Create user
            userDAO.createUser(userData);

            // Get user with wrong password, should return null
            String wrongPassword = "iamugly";
            UserData foundUserData = userDAO.getUser(username, wrongPassword);

            Assertions.assertNull(foundUserData);

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(5)
    @DisplayName("Create Game Success")
    public void createGameSuccess() {
        // Create game, verify no errors are thrown
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame(gameData));
    }

    @Test
    @Order(6)
    @DisplayName("Create Game Failure")
    public void createGameFailure() {
        try {
            // Create game
            gameDAO.createGame(gameData);

            // Try to create same game, should fail primary key condition
            Assertions.assertThrows(DataAccessException.class, () -> gameDAO.createGame(gameData));

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(7)
    @DisplayName("Get Game Success")
    public void getGameSuccess() {
        try {
            // Create game
            gameDAO.createGame(gameData);

            // Verify gameName from database is not null
            Assertions.assertEquals(gameDAO.getGame(gameID).getGameName(), gameName);

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(8)
    @DisplayName("Get Move Failure")
    public void getGameFailure() {
        try {
            // Create game
            gameDAO.createGame(gameData);

            // Verify gameName from database is null after fetching with wrong id
            Assertions.assertNull(gameDAO.getGame(2).getGameName());

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(9)
    @DisplayName("Add Player Success")
    public void addPlayerSuccess() {
        try {
            // Create game
            gameDAO.createGame(gameData);

            // Create user
            userDAO.createUser(userData);

            // Update game with username for whiteUsername
            gameData.setWhiteUsername(username);
            gameDAO.updateGame(gameData);

            // Get game
            GameData foundGame = gameDAO.getGame(gameID);

            // Assert whiteUsername is updated correctly
            Assertions.assertEquals(foundGame.getWhiteUsername(), username);

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(10)
    @DisplayName("Add Player Failure")
    public void addPlayerFailure() {
        try {
            // Create game
            gameDAO.createGame(gameData);

            // Update game without username attached to a user for whiteUsername
            gameData.setWhiteUsername(username);
            Assertions.assertThrows(DataAccessException.class, () -> gameDAO.updateGame(gameData));

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(11)
    @DisplayName("Make Move Success")
    public void makeMoveSuccess() {
        try {
            // Create game
            gameDAO.createGame(gameData);


            // Make move
            ChessPosition startPos = new ChessPosition(2, 4);
            ChessPosition endPos = new ChessPosition(4, 4);
            ChessMove move = new ChessMove(startPos, endPos, null);
            game.makeMove(move);

            // Update game in database
            gameData.setGame(game);
            gameDAO.updateGame(gameData);

            // Get game and its board
            GameData foundGame = gameDAO.getGame(gameID);
            ChessBoard foundBoard = foundGame.getGame().getBoard();

            // Verify move registered
            Assertions.assertNotNull(foundBoard.getPiece(endPos));

        } catch (DataAccessException | InvalidMoveException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(12)
    @DisplayName("Make Move Failure")
    public void makeMoveFailure() {
        try {
            // Create 2 games
            gameDAO.createGame(gameData);
            gameDAO.createGame(gameData2);


            // Make move for game 2
            ChessPosition startPos = new ChessPosition(2, 4);
            ChessPosition endPos = new ChessPosition(4, 4);
            ChessMove move = new ChessMove(startPos, endPos, null);
            game2.makeMove(move);

            // Update game2 in database
            gameData2.setGame(game2);
            gameDAO.updateGame(gameData2);

            // Get game and its board
            GameData foundGame = gameDAO.getGame(gameID);
            ChessBoard foundBoard = foundGame.getGame().getBoard();

            // Verify move did not register for game 2
            Assertions.assertNull(foundBoard.getPiece(endPos));

        } catch (DataAccessException | InvalidMoveException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @Order(13)
    @DisplayName("List Games Success")
    public void listGamesSuccess() {
        try {
            // Create 2 games
            gameDAO.createGame(gameData);
            gameDAO.createGame(gameData2);

            // Assert lists both games
            Assertions.assertEquals(gameDAO.listGames().size(), 2);

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(14)
    @DisplayName("List Games Failure")
    public void listGamesFailure() {
        try {
            // Assert list games is empty
            Assertions.assertEquals(gameDAO.listGames().size(), 0);

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(15)
    @DisplayName("Create Auth Success")
    public void createAuthSuccess() {
        try {
            // Create user
            userDAO.createUser(userData);

            // Create auth, make sure no errors are thrown
            Assertions.assertDoesNotThrow(() -> authDAO.createAuth(authData));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(16)
    @DisplayName("Create Auth Failure")
    public void createAuthFailure() {
        try {
            // Create user
            userDAO.createUser(userData);

            // Create auth
            authDAO.createAuth(authData);

            // Try creating auth again, should fail primary key condition
            Assertions.assertThrows(DataAccessException.class, () -> authDAO.createAuth(authData));

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(17)
    @DisplayName("Get Auth Success")
    public void getAuthSuccess() {
        try {
            // Create user
            userDAO.createUser(userData);

            // Create auth
            authDAO.createAuth(authData);

            // Assert found authToken is equal to the one assigned
            Assertions.assertEquals(authToken, authDAO.getAuth(authToken).authToken());

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(18)
    @DisplayName("Get Auth Failure")
    public void getAuthFailure() {
        try {
            // Create user
            userDAO.createUser(userData);

            // Create auth
            authDAO.createAuth(authData);

            // Assert found authToken is null using wrong authToken
            Assertions.assertNull(authDAO.getAuth("keep me out").authToken());

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(19)
    @DisplayName("Delete Auth Success")
    public void deleteAuthSuccess() {
        try {
            // Create user
            userDAO.createUser(userData);

            // Create auth
            authDAO.createAuth(authData);

            // Delete auth
            authDAO.deleteAuth(authData);

            // Assert found authToken is null after deleting
            Assertions.assertNull(authDAO.getAuth(authToken).authToken());

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(20)
    @DisplayName("Delete Auth Failure")
    public void deleteAuthFailure() {
        try {
            // Create user
            userDAO.createUser(userData);

            // Create auth
            authDAO.createAuth(authData);

            // Create new authData and delete it
            AuthData wrongAuthData = new AuthData("keep me out", username);
            authDAO.deleteAuth(wrongAuthData);

            // Assert found authToken is not null after deleting wrong authData
            Assertions.assertNotNull(authDAO.getAuth(authToken).authToken());

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(21)
    @DisplayName("User Clear Success")
    public void userClearSuccess() {
        try {
            // Create user
            userDAO.createUser(userData);

            // Clear table
            userDAO.clearUser();

            // Attempt to get user, verify found userData is null
            Assertions.assertNull(userDAO.getUser(username, password));

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(22)
    @DisplayName("Game Clear Success")
    public void gameClearSuccess() {
        try {
            // Create 2 games
            gameDAO.createGame(gameData);
            gameDAO.createGame(gameData2);

            // Clear table
            gameDAO.clearGame();

            // Assert no games are listed
            Assertions.assertEquals(gameDAO.listGames().size(), 0);

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(23)
    @DisplayName("Auth Clear Success")
    public void authClearSuccess() {
        try {
            // Create user
            userDAO.createUser(userData);

            // Create auth
            authDAO.createAuth(authData);

            // Clear table
            authDAO.clearAuth();

            // Assert found authToken is null after clearing
            Assertions.assertNull(authDAO.getAuth(authToken).authToken());

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
