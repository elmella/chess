package chess;

import java.util.ArrayList;

public class RookPiece extends ChessPiece {

    private final int row;
    private final int col;

    private final BooleanWrapper upBlocked = new BooleanWrapper(false);
    private final BooleanWrapper rightBlocked = new BooleanWrapper(false);
    private final BooleanWrapper downBlocked = new BooleanWrapper(false);
    private final BooleanWrapper leftBlocked = new BooleanWrapper(false);

    private final ChessBoard board;
    private final ChessPosition myPosition;
    private final ChessGame.TeamColor myTeam;
    private final ArrayList<ChessMove> rookMoves;


    public RookPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, ChessBoard board, ChessPosition myPosition) {
        super(pieceColor, type);
        row = myPosition.getRow();
        col = myPosition.getColumn();
        this.board = board;
        this.myPosition = myPosition;
        myTeam = board.getPiece(myPosition).getTeamColor();
        rookMoves = new ArrayList<>();
        rookMoves();
    }

    public ArrayList<ChessMove> getRookMoves() {
        return rookMoves;
    }


    private void rookMoves() {
        // Add up moves
        int max = 8;
        int min = 1;
        for (int i = min; i <= max - row; i++) {
            // Up
            addRookMoves(upBlocked, row + i, col);
        }

        // Add down moves
        for (int i = min; i <= row - min; i++) {
            addRookMoves(downBlocked, row - i, col);
        }

        // Right
        for (int i = min; i <= max - col; i++) {
            addRookMoves(rightBlocked, row, col + i);
        }

        // Left
        for (int i = min; i <= col - min; i++) {
            addRookMoves(leftBlocked, row, col - i);
        }
    }

    private void addRookMoves(BooleanWrapper blocked, int row, int col) {
        if (!blocked.value) {
            ChessPosition pos = new ChessPosition(row, col);
            ChessMove move = new ChessMove(myPosition, pos, null);
            if (board.getPiece(pos) != null) {
                blocked.value = true;
                if (board.getPiece(pos).getTeamColor() != myTeam) {
                    rookMoves.add(move);
                }
            } else {
                rookMoves.add(move);
            }
        }
    }
}
