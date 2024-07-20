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

    @BeforeEach
    public void clear() throws DataAccessException {
        clear.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Create User Success")
    public void createUserSuccess() {
        String username = "emily";
        String password = "iambeautiful";
        String email = "email@email.com";
        UserData userData = new UserData(username, password, email);
        // Create user, make sure no errors are thrown
        Assertions.assertDoesNotThrow(() -> userDAO.createUser(userData));
    }

    @Test
    @Order(2)
    @DisplayName("Create User Failure")
    public void createUserFailure() {
        String username = "emily";
        String password = "iambeautiful";
        String email = "email@email.com";
        UserData userData = new UserData(username, password, email);
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
        String username = "emily";
        String password = "iambeautiful";
        String email = "email@email.com";
        UserData userData = new UserData(username, password, email);
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
        String username = "emily";
        String password = "iambeautiful";
        String wrongPassword = "iamugly";
        String email = "email@email.com";
        UserData userData = new UserData(username, password, email);
        try {
            // Create user
            userDAO.createUser(userData);

            // Get user with wrong password, should return null
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
        int gameID = 1;
        String gameName = "game";
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(gameID, null, null, gameName, game);
        // Create game, verify no errors are thrown
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame(gameData));
    }

    @Test
    @Order(6)
    @DisplayName("Create Game Failure")
    public void createGameFailure() {
        int gameID = 1;
        String gameName = "game";
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(gameID, null, null, gameName, game);
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
        int gameID = 1;
        String gameName = "game";
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(gameID, null, null, gameName, game);
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
        int gameID = 1;
        String gameName = "game";
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(gameID, null, null, gameName, game);
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
        int gameID = 1;
        String gameName = "game";
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(gameID, null, null, gameName, game);
        try {
            // Create game
            gameDAO.createGame(gameData);

            // Create user
            String username = "emily";
            UserData userData = new UserData(username, "iambeautiful", "email@email.com");
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
        int gameID = 1;
        String gameName = "game";
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(gameID, null, null, gameName, game);
        try {
            // Create game
            gameDAO.createGame(gameData);

            // Do not create user
            String username = "emily";

            // Update game with username not attached to a user for whiteUsername
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
        int gameID = 1;
        String gameName = "game";
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(gameID, null, null, gameName, game);
        try {
            // Create game
            gameDAO.createGame(gameData);


            // Make move
            ChessPosition startPos = new ChessPosition(2,4);
            ChessPosition endPos = new ChessPosition(4,4);
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
        int gameID1 = 1;
        int gameID2 = 2;
        String gameName1 = "game1";
        String gameName2 = "game2";
        ChessGame game1 = new ChessGame();
        ChessGame game2 = new ChessGame();
        GameData gameData1 = new GameData(gameID1, null, null, gameName1, game1);
        GameData gameData2 = new GameData(gameID2, null, null, gameName2, game2);
        try {
            // Create 2 games
            gameDAO.createGame(gameData1);
            gameDAO.createGame(gameData2);


            // Make move for game 1
            ChessPosition startPos = new ChessPosition(2,4);
            ChessPosition endPos = new ChessPosition(4,4);
            ChessMove move = new ChessMove(startPos, endPos, null);
            game1.makeMove(move);

            // Update game in database
            gameData1.setGame(game1);
            gameDAO.updateGame(gameData1);

            // Get game2 and its board
            GameData foundGame = gameDAO.getGame(gameID2);
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
        int gameID1 = 1;
        int gameID2 = 2;
        String gameName1 = "game1";
        String gameName2 = "game2";
        ChessGame game1 = new ChessGame();
        ChessGame game2 = new ChessGame();
        GameData gameData1 = new GameData(gameID1, null, null, gameName1, game1);
        GameData gameData2 = new GameData(gameID2, null, null, gameName2, game2);
        try {
            // Create 2 games
            gameDAO.createGame(gameData1);
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
        String username = "emily";
        String password = "iambeautiful";
        String email = "email@email.com";
        UserData userData = new UserData(username, password, email);
        try {
            // Create user
            userDAO.createUser(userData);

            // Create auth for user
            String authToken = "let me in";
            AuthData authData = new AuthData(authToken, username);
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
        String username = "emily";
        String password = "iambeautiful";
        String email = "email@email.com";
        UserData userData = new UserData(username, password, email);
        try {
            // Create user
            userDAO.createUser(userData);

            // Create auth for user
            String authToken = "let me in";
            AuthData authData = new AuthData(authToken, username);
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
        String username = "emily";
        String password = "iambeautiful";
        String email = "email@email.com";
        UserData userData = new UserData(username, password, email);
        try {
            // Create user
            userDAO.createUser(userData);

            // Create auth for user
            String authToken = "let me in";
            AuthData authData = new AuthData(authToken, username);

            // Create auth, make sure no errors are thrown
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
        String username = "emily";
        String password = "iambeautiful";
        String email = "email@email.com";
        UserData userData = new UserData(username, password, email);
        try {
            // Create user
            userDAO.createUser(userData);

            // Create auth for user
            String authToken = "let me in";
            AuthData authData = new AuthData(authToken, username);

            // Create auth, make sure no errors are thrown
            authDAO.createAuth(authData);

            // Assert found authToken is null using wrong authToken
            Assertions.assertEquals(authToken, authDAO.getAuth(authToken).authToken());

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

}
