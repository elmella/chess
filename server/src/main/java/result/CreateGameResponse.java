package request;

import result.Response;

public class CreateGameResponse extends Response {

    /**
     * gameName : int
     */
    private final int gameID;

    /**
     * constructor for CreateGameRequest for a successful request
     *
     * @param gameID : int
     */
    public CreateGameResponse(int gameID, boolean success, String message) {
        super(message, success);
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
