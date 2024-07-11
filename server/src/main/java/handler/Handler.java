package handler;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import service.AuthService;
import spark.Request;
import spark.Response;

import java.lang.reflect.Field;

public class Handler {

    public boolean authorize(Request req, Response res) {

        String authToken = req.headers("authorization");
        AuthService auth = new AuthService(MemoryAuthDAO.getInstance());

        boolean authorized = false;
        try {
            authorized = auth.authorize(authToken);
        } catch (DataAccessException e) {
            res.status(500);
            return false;
        }
        if (!authorized) {
            res.status(401);
        }
        return authorized;
    }

    public boolean hasNullFields(Object obj) {
        if (obj == null) {
            return true;
        }

        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.get(obj) == null) {
                    return true;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
