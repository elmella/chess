package dataaccess;

import model.AuthData;

import java.util.ArrayList;

public class MemoryAuthDAO implements AuthDAOInterface {
    ArrayList<AuthData> auth;

    public MemoryAuthDAO() {
        auth = new ArrayList<>();
    }

    @Override
    public void createAuth(AuthData a) throws DataAccessException {
        if (auth.contains(a)) {
            throw new DataAccessException("Auth already exists");
        }
        auth.add(a);
    }

    @Override
    public AuthData getAuth(String username) throws DataAccessException {
        AuthData a;
        for (AuthData data : this.auth) {
            if (data.username().equals(a.username())) {
                return a;
            }
        }
        throw new DataAccessException("Username not found");
    }

    @Override
    public void deleteAuth(AuthData a) throws DataAccessException {
        if (!auth.contains(a)) {
            throw new DataAccessException("Auth not found");
        }
        auth.remove(a);
    }
}
