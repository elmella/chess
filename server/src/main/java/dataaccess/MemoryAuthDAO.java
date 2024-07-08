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

    }

    @Override
    public AuthData getAuth(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(AuthData a) throws DataAccessException {

    }
}
