package websocket.messages;

import chess.ChessGame;
import model.GameData;

public class LoadGameMessage extends ServerMessage {

    private GameData gameData;
    public LoadGameMessage(ServerMessageType type, GameData gameData) {
        super(type);
        this.gameData = gameData;
    }

    public GameData getGame() {
        return gameData;
    }

    public void setGame(GameData gameData) {
        this.gameData = gameData;
    }
}
