package dataaccess;
import dataaccess.*;
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
    private final ClearService clear = new ClearService(AuthDAO.getInstance(), MemoryGameDAO.getInstance(), UserDAO.getInstance());

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
            System.out.println(foundUserData);

            Assertions.assertEquals(username, foundUserData.username());

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }



}
