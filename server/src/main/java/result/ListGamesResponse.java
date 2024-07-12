package result;

import java.util.ArrayList;

public class ListGamesResponse extends Response {

    /**
     * gameResponses : ArrayList<GameResponse>
     */
    private ArrayList<GameResponse> games;

    /**
     * constructor for ListGamesResponse for a successful request
     *
     * @param gameResponses : ArrayList<GameResponse>
     */
    public ListGamesResponse(ArrayList<GameResponse> gameResponses, boolean success, String message) {
        super(message, success);
        this.games = gameResponses;
    }

    public ArrayList<GameResponse> getGameResponses() {
        return games;
    }



    @Override
    public String toString() {
        return "ListGamesResponse{" + "games=" + games + '}';
    }
}