package dataaccess;
import chess.ChessGame;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import service.ClearService;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DAOTests {

    private final MemoryAuthDAO authDAO = new MemoryAuthDAO();
    private final UserDAO userDAO = new UserDAO();
    private final GameDAO gameDAO = new GameDAO();
    private final ClearService clear = new ClearService(MemoryAuthDAO.getInstance(), GameDAO.getInstance(), UserDAO.getInstance());

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

            // Get user
            UserData foundUserData = userDAO.getUser(username, password);

            Assertions.assertEquals(username, foundUserData.username());

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
    @DisplayName("Make Move Failure")
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
    @DisplayName("Update Game Success")
    public void updateGameSuccess() {
        int gameID = 1;
        String whiteUsername = null;
        String blackUsername = null;
        String gameName = "game";
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        try {
            // Create game
            gameDAO.createGame(gameData);

            // Get game
            GameData foundGameData = gameDAO.getGame(gameID);
            System.out.println(foundGameData.getGameID());

            Assertions.assertEquals(gameID, foundGameData.getGameID());

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
