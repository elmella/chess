package result;

public class CreateGameResponse extends Response {

    /**
     * gameName : String
     */
    private String gameName;

    /**
     * constructor for CreateGameResponse for a successful request
     *
     * @param gameName : String
     */
    public CreateGameResponse(String gameName) {
        this.gameName = gameName;
    }

    /**
     * constructor for CreateGameResponse for an unsuccessful request
     *
     * @param success : boolean
     * @param message : String
     */
    public CreateGameResponse(String message, boolean success) {
        super(message, success);
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
