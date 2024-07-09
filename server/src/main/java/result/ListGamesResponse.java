package result;

import java.util.ArrayList;

public class ListGamesResponse extends Response {

    /**
     * gameResponses : ArrayList<GameResponse>
     */
    private ArrayList<GameResponse> gameResponses;

    /**
     * constructor for ListGamesResponse for a successful request
     *
     * @param gameResponses : ArrayList<GameResponse>
     */
    public ListGamesResponse(ArrayList<GameResponse> gameResponses) {
        this.gameResponses = gameResponses;
    }

    /**
     * constructor for ListGamesResponse for an unsuccessful request
     *
     * @param success : boolean
     * @param message : String
     */
    public ListGamesResponse(String message, boolean success) {
        super(message, success);
    }

    public ArrayList<GameResponse> getGameResponses() {
        return gameResponses;
    }

    public void setGameResponses(ArrayList<GameResponse> gameResponses) {
        this.gameResponses = gameResponses;
    }
}