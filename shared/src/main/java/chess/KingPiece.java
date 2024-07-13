package chess;

import java.util.ArrayList;

public class KingPiece extends ChessPiece {

    private final int row;
    private final int col;
    private final boolean leftEdge;
    private final boolean rightEdge;
    private final boolean bottomEdge;
    private final boolean topEdge;

    private final ChessBoard board;
    private final ChessPosition myPosition;

    private final ArrayList<ChessMove> kingMoves;
    private final ChessGame.TeamColor myTeam;


    public KingPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, ChessBoard board, ChessPosition myPosition) {
        super(pieceColor, type);
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int min = 1;
        int max = 8;
        leftEdge = (col == min);
        rightEdge = (col == max);
        bottomEdge = (row == min);
        topEdge = (row == max);
        this.col = col;
        this.row = row;
        this.board = board;
        this.myPosition = myPosition;
        myTeam = board.getPiece(myPosition).getTeamColor();
        kingMoves = new ArrayList<>();
        kingMoves();
    }

    public ArrayList<ChessMove> getKingMoves() {
        return kingMoves;
    }

    private void kingMoves() {
        // Add down moves
        if (!bottomEdge) {
            // Down left
            addMoves(leftEdge, row - 1, col - 1);

            // Down right
            addMoves(rightEdge, row - 1, col + 1);


            // Down
            addMoves(false, row - 1, col);

        }

        // Add up moves
        if (!topEdge) {
            // Up left
            addMoves(leftEdge, row + 1, col - 1);

            // Up right
            addMoves(rightEdge, row + 1, col + 1);


            // Up
            addMoves(false, row + 1, col);
        }

        // Right
        addMoves(rightEdge, row, col + 1);

        // Left
        addMoves(leftEdge, row, col - 1);

    }

    private void addMoves(boolean blocked, int row, int col) {
        if (!blocked) {
            ChessPosition pos = new ChessPosition(row, col);
            ChessMove move = new ChessMove(myPosition, pos, null);
            if (board.getPiece(pos) != null) {
                if (board.getPiece(pos).getTeamColor() != myTeam) {
                    kingMoves.add(move);
                }
            } else {
                kingMoves.add(move);
            }
        }
    }
}
