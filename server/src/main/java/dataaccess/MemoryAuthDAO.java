package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public class MemoryAuthDAO implements AuthDAOInterface {
    private static MemoryAuthDAO instance;
    ArrayList<AuthData> auth;


    public MemoryAuthDAO() {
        auth = new ArrayList<>();
    }

    public static MemoryAuthDAO getInstance() {
        if (instance == null) {
            instance = new MemoryAuthDAO();
        }
        return instance;
    }

    @Override
    public void createAuth(AuthData a) throws DataAccessException {
        auth.add(a);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        for (AuthData a : this.auth) {
            if (a.authToken().equals(authToken)) {
                return a;
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(AuthData a) throws DataAccessException {
        auth.remove(a);
    }

    @Override
    public void clearAuth() throws DataAccessException {
        auth = new ArrayList<>();
    }

}
