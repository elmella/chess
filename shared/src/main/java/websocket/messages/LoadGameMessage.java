package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {

    private ChessGame game;
    private String checkNotification;
    private String checkmateNotification;
    private String stalemateNotification;
    private String moveNotification;
    public LoadGameMessage(ServerMessageType type, ChessGame game, String checkNotification, String checkmateNotification, String stalemateNotification, String moveNotification) {
        super(type);
        this.game = game;
        this.checkNotification = checkNotification;
        this.checkmateNotification = checkmateNotification;
        this.stalemateNotification = stalemateNotification;
        this.moveNotification = moveNotification;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

    public String getCheckNotification() {
        return checkNotification;
    }

    public void setCheckNotification(String checkNotification) {
        this.checkNotification = checkNotification;
    }

    public String getCheckmateNotification() {
        return checkmateNotification;
    }

    public void setCheckmateNotification(String checkmateNotification) {
        this.checkmateNotification = checkmateNotification;
    }

    public String getStalemateNotification() {
        return stalemateNotification;
    }

    public void setStalemateNotification(String stalemateNotification) {
        this.stalemateNotification = stalemateNotification;
    }

    public String getMoveNotification() {
        return moveNotification;
    }

    public void setMoveNotification(String moveNotification) {
        this.moveNotification = moveNotification;
    }
}
