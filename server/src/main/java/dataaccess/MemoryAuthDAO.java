package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public class MemoryAuthDAO implements AuthDAOInterface {
    ArrayList<AuthData> auth;
    private static MemoryAuthDAO instance;


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
    public AuthData getAuth(String authToke) throws DataAccessException {
        for (AuthData a : this.auth) {
            if (a.authToken().equals(authToke)) {
                return a;
            }
        }
        throw new DataAccessException("AuthToken not found");
    }

    @Override
    public void deleteAuth(AuthData a) throws DataAccessException {
        if (!auth.contains(a)) {
            throw new DataAccessException("Auth not found");
        }
        auth.remove(a);
    }

    @Override
    public void clearAuth() throws DataAccessException {
        auth = new ArrayList<>();
    }
}
