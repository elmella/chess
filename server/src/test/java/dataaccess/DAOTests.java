package dataaccess;
import chess.ChessGame;
import dataaccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import request.*;
import request.CreateGameResponse;
import result.ListGamesResponse;
import result.LoginResponse;
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
    @Order(2)
    @DisplayName("Create User Failure")
    public void createUserFailure() {
        String username = "emily";
        String password = "iambeautiful";
        String wrongPassword = "iamugly";
        String email = "email@email.com";
        UserData userData = new UserData(username, password, email);
        try {
            // Create user
            userDAO.createUser(userData);

            // Get user
            UserData foundUserData = userDAO.getUser(username, wrongPassword);

            Assertions.assertNull(foundUserData);

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(3)
    @DisplayName("Create Game Success")
    public void createGameSuccess() {
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

    @Test
    @Order(4)
    @DisplayName("Make Move Failure")
    public void createGameFailure() {
        int gameID = 1;
        String whiteUsername = null;
        String blackUsername = null;
        String gameName = "game";
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        try {
            // Create game
            gameDAO.createGame(gameData);

            // Try to create same game, should fail primary key condition
            Assertions.assertThrows(DataAccessException.class, () -> {
                gameDAO.createGame(gameData);
            });

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(3)
    @DisplayName("Make Move Success")
    public void createGameSuccess() {
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
