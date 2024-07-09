package result;

public class JoinGameResponse extends Response {

    /**
     * playerColor : String
     */
    private String playerColor;
    /**
     * gameID : int
     */
    private int gameID;

    /**
     * constructor for JoinGameResponse for a successful request
     *
     * @param playerColor : String
     * @param gameID : int
     */
    public JoinGameResponse(String playerColor, int gameID) {
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    /**
     * constructor for JoinGameResponse for an unsuccessful request
     *
     * @param success : boolean
     * @param message : String
     */
    public JoinGameResponse(String message, boolean success) {
        super(message, success);
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }
}
