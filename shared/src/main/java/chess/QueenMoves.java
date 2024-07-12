package chess;

import java.util.ArrayList;

public class QueenMoves extends ChessPiece {

    private final int min = 1;
    private final int max = 8;
    private final int row;
    private final int col;
    private boolean leftEdge;
    private boolean rightEdge;
    private final boolean bottomEdge;
    private final boolean topEdge;

    private final BooleanWrapper upBlocked = new BooleanWrapper(false);
    private final BooleanWrapper upRightBlocked = new BooleanWrapper(false);
    private final BooleanWrapper rightBlocked = new BooleanWrapper(false);
    private final BooleanWrapper downRightBlocked = new BooleanWrapper(false);
    private final BooleanWrapper downBlocked = new BooleanWrapper(false);
    private final BooleanWrapper downLeftBlocked = new BooleanWrapper(false);
    private final BooleanWrapper leftBlocked = new BooleanWrapper(false);
    private final BooleanWrapper upLeftBlocked = new BooleanWrapper(false);

    private final ChessBoard board;
    private final ChessPosition myPosition;

    private ArrayList<ChessMove> queenMoves = new ArrayList<>();
    private final ChessGame.TeamColor myTeam;



    public QueenMoves(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type,
                      ChessBoard board, ChessPosition myPosition, int col, int row) {
        super(pieceColor, type);
        leftEdge = (col == min);
        rightEdge = (col == max);
        bottomEdge = (row == min);
        topEdge = (row == max);
        this.col = col;
        this.row = row;
        this.board = board;
        this.myPosition = myPosition;
        myTeam = board.getPiece(myPosition).getTeamColor();

        queenMoves = queenMoves();

    }

    public ArrayList<ChessMove> getQueenMoves() {
        return queenMoves;
    }


    private ArrayList<ChessMove> queenMoves() {
        // Add up moves
        for (int i = 1; i <= max - row; i++) {
            // Declare if at the edge
            leftEdge = (col - i < min);
            rightEdge = (col + i > max);

            // Up left
            if (!leftEdge && !upLeftBlocked.value) {
                addMoves(upLeftBlocked,row + i, col - i);
            }
            // Up right
            if (!rightEdge && !upRightBlocked.value) {
                addMoves(upRightBlocked,row + i, col + i);
            }

            // Up
            if (!upBlocked.value) {
                addMoves(upBlocked,row + i, col);
            }
        }

        // Add down moves
        for (int i = 1; i <= row - min; i++) {
            // Declare if at the edge
            leftEdge = (col - i < min);
            rightEdge = (col + i > max);

            // Down left
            if (!leftEdge && !downLeftBlocked.value) {
                addMoves(downLeftBlocked,row - i, col - i);
            }
            // Down right
            if (!rightEdge && !downRightBlocked.value) {
                addMoves(downRightBlocked,row - i, col + i);
            }

            // Down
            if (!downBlocked.value) {
                addMoves(downBlocked, row - i, col);

            }
        }

        // Right
        for (int i = 1; i <= max - col; i++) {
            if (!rightBlocked.value) {
                addMoves(rightBlocked, row, col + i);

            }
        }

        // Left
        for (int i = 1; i <= col - min; i++) {
            if (!leftBlocked.value) {
                addMoves(leftBlocked, row, col - i);

            }
        }
        return queenMoves;
    }

    private void addMoves(BooleanWrapper blocked, int row, int col) {
            ChessPosition pos = new ChessPosition(row, col);
            ChessMove move = new ChessMove(myPosition, pos, null);
            if (board.getPiece(pos) != null) {
                blocked.value = true;
                if (board.getPiece(pos).getTeamColor() != myTeam) {
                    queenMoves.add(move);
                }
            } else {
                queenMoves.add(move);
            }
    }
}
