package handler;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {

    public String handleRequest(Request req, Response res) {

        ClearService clear = new ClearService(MemoryAuthDAO.getInstance(), MemoryGameDAO.getInstance(), MemoryUserDAO.getInstance());

        result.Response result = clear.clear();

        if (!result.isSuccess()) {
            res.status(500);
        }
        return UseGson.toJson(result);
    }

}
