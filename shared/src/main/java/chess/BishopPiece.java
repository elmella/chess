package chess;

import java.util.ArrayList;

public class BishopPiece extends ChessPiece {

    private final int min = 1;
    private final int max = 8;
    private final int row;
    private final int col;
    private boolean leftEdge;
    private boolean rightEdge;

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
    private final ChessGame.TeamColor myTeam;
    private final ArrayList<ChessMove> bishopMoves;



    public BishopPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type,
                      ChessBoard board, ChessPosition myPosition) {
        super(pieceColor, type);
        row = myPosition.getRow();
        col = myPosition.getColumn();
        leftEdge = (col == min);
        rightEdge = (col == max);
        this.board = board;
        this.myPosition = myPosition;
        myTeam = board.getPiece(myPosition).getTeamColor();
        bishopMoves = new ArrayList<>();
        bishopMoves();
    }

    public ArrayList<ChessMove> getBishopMoves() {
        return bishopMoves;
    }


    private void bishopMoves() {
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
        }
    }

    private void addMoves(BooleanWrapper blocked, int row, int col) {
        ChessPosition pos = new ChessPosition(row, col);
        ChessMove move = new ChessMove(myPosition, pos, null);
        if (board.getPiece(pos) != null) {
            blocked.value = true;
            if (board.getPiece(pos).getTeamColor() != myTeam) {
                bishopMoves.add(move);
            }
        } else {
            bishopMoves.add(move);
        }
    }
}