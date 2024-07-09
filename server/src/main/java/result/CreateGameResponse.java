package request;

import result.Response;

public class CreateGameResponse extends Response {

    /**
     * gameName : int
     */
    private int gameID;

    /**
     * constructor for CreateGameRequest for a successful request
     *
     * @param gameID : int
     */
    public CreateGameResponse(int gameID) {
        this.gameID = gameID;
    }

    /**
     * constructor for CreateGameRequest for an unsuccessful request
     *
     * @param success : boolean
     * @param message : String
     */
    public CreateGameResponse(String message, boolean success) {
        super(message, success);
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
