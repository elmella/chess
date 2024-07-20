package chess;

import java.util.ArrayList;

public class KnightPiece extends ChessPiece {

    private final int min = 1;
    private final int max = 8;
    private final int row;
    private final int col;
    private final ChessBoard board;
    private final ChessPosition myPosition;
    private final ChessGame.TeamColor myTeam;
    private final ArrayList<ChessMove> knightMoves;
    boolean leftEdge;
    boolean rightEdge;
    boolean bottomEdge;
    boolean topEdge;


    public KnightPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, ChessBoard board, ChessPosition myPosition) {
        super(pieceColor, type);
        row = myPosition.getRow();
        col = myPosition.getColumn();
        leftEdge = (col == min);
        rightEdge = (col == max);
        bottomEdge = (row == min);
        topEdge = (row == max);
        this.board = board;
        this.myPosition = myPosition;
        myTeam = board.getPiece(myPosition).getTeamColor();
        knightMoves = new ArrayList<>();
        knightMoves();
    }

    public ArrayList<ChessMove> getKnightMoves() {
        return knightMoves;
    }


    private void knightMoves() {

        // 2+ below max
        if (row < max - 1) {

            // 2 up, 1 right
            addKnightMoves(rightEdge, row + 2, col + 1);

            // 2 up, 1 left
            addKnightMoves(leftEdge, row + 2, col - 1);
        }

        // 2+ above min
        if (row > min + 1) {

            // 2 down, 1 right
            addKnightMoves(rightEdge, row - 2, col + 1);

            // 2 down, 1 left
            addKnightMoves(leftEdge, row - 2, col - 1);
        }

        // 2+ left max
        if (col < max - 1) {

            // 1 up, 2 right
            addKnightMoves(topEdge, row + 1, col + 2);

            // 1 down, 2 right
            addKnightMoves(bottomEdge, row - 1, col + 2);
        }

        // 2+ right min
        if (col > min + 1) {

            // 1 up, 2 left
            addKnightMoves(topEdge, row + 1, col - 2);

            // 1 down, 2 left
            addKnightMoves(bottomEdge, row - 1, col - 2);
        }
    }

    private void addKnightMoves(boolean edge, int row, int col) {
        if (!edge) {
            ChessPosition pos = new ChessPosition(row, col);
            ChessMove move = new ChessMove(myPosition, pos, null);
            if (board.getPiece(pos) != null) {
                if (board.getPiece(pos).getTeamColor() != myTeam) {
                    knightMoves.add(move);
                }
            } else {
                knightMoves.add(move);
            }
        }
    }
}
