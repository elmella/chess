package handler;

import dataaccess.DataAccessException;
import dataaccess.AuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.UserDAO;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {

    public String handleRequest(Request req, Response res) {

        ClearService clear = new ClearService(AuthDAO.getInstance(), MemoryGameDAO.getInstance(), UserDAO.getInstance());

        result.Response result = null;
        try {
            result = clear.clear();
        } catch (DataAccessException e) {
            res.status(500);
            return UseGson.toJson(new result.Response(e.getMessage(), false));
        }

        return UseGson.toJson(result);
    }

}
