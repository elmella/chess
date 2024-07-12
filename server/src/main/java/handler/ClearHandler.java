package handler;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {

    public String handleRequest(Request req, Response res) {

        ClearService clear = new ClearService(MemoryAuthDAO.getInstance(), MemoryGameDAO.getInstance(), MemoryUserDAO.getInstance());

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
