package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResponse;
import result.Response;

import java.util.UUID;

public class UserService extends Service {
    private final UserDAOInterface userDAO;
    private final AuthDAOInterface authDAO;

    public UserService(UserDAOInterface userDAO, AuthDAOInterface authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public LoginResponse register(RegisterRequest request) throws DataAccessException, AlreadyTakenException, UnauthorizedException, BadRequestException {
        hasNullFields(request);
        // Check if user exists
        UserData foundUser = userDAO.getUser(request.username(), request.password());

        if (foundUser == null) {

            // Create new user
            UserData user = new UserData(request.username(), request.password(), request.email());
            userDAO.createUser(user);

            // Login new user
            LoginRequest loginRequest = new LoginRequest(request.username(), request.password());
            return login(loginRequest);
        }

//        return new LoginResponse(null, null, false, "Error: already taken");
        throw new AlreadyTakenException("Error: already taken");
    }

    public LoginResponse login(LoginRequest request) throws DataAccessException, UnauthorizedException {
        String username = request.username();
        // Check if user exists
        UserData foundUser = userDAO.getUser(username, request.password());

        if (foundUser != null) {

            // Generate authToken
            String authToken = UUID.randomUUID().toString();
            AuthData auth = new AuthData(authToken, username);

            // Create auth
            authDAO.createAuth(auth);
            return new LoginResponse(authToken, username, true, null);
        }
        throw new UnauthorizedException("Error: unauthorized");
    }

    public Response logout(String authToken) throws DataAccessException, UnauthorizedException {
        AuthData auth = authDAO.getAuth(authToken);
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        authDAO.deleteAuth(auth);
        return new Response(null, true);
    }
}
