package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {

    private ChessGame game;
    private boolean isCheck;
    private boolean isCheckmate;
    private boolean isStalemate;
    public LoadGameMessage(ServerMessageType type, ChessGame game, boolean isCheck, boolean isCheckmate, boolean isStalemate) {
        super(type);
        this.game = game;
        this.isCheck = isCheck;
        this.isCheckmate = isCheckmate;
        this.isStalemate = isStalemate;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isCheckmate() {
        return isCheckmate;
    }

    public void setCheckmate(boolean checkmate) {
        isCheckmate = checkmate;
    }

    public boolean isStalemate() {
        return isStalemate;
    }

    public void setStalemate(boolean stalemate) {
        isStalemate = stalemate;
    }
}
