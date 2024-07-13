package chess;

import java.util.ArrayList;

public class PawnPiece extends ChessPiece {

    private final int row;
    private final int col;
    private final ChessBoard board;
    private final ChessPosition myPosition;
    private final ChessGame.TeamColor myTeam;
    private final ArrayList<ChessMove> pawnMoves;
    boolean leftEdge;
    boolean rightEdge;
    boolean bottomEdge;
    boolean topEdge;


    public PawnPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, ChessBoard board, ChessPosition myPosition) {
        super(pieceColor, type);
        row = myPosition.getRow();
        col = myPosition.getColumn();
        int min = 1;
        leftEdge = (col == min);
        int max = 8;
        rightEdge = (col == max);
        bottomEdge = (row == min);
        topEdge = (row == max);
        this.board = board;
        this.myPosition = myPosition;
        myTeam = board.getPiece(myPosition).getTeamColor();
        pawnMoves = new ArrayList<>();
        pawnMoves();
    }

    public ArrayList<ChessMove> getPawnMoves() {
        return pawnMoves;
    }


    private void pawnMoves() {

        int direction = 1;
        int startRow = 2;
        int endRow = 8;

        if (myTeam == ChessGame.TeamColor.BLACK) {
            direction = -1;
            startRow = 7;
            endRow = 1;
        }

        boolean couldPromote = (row + direction == endRow);

        // Start advancement
        if (row == startRow) {
            ChessPosition singlePos = new ChessPosition(row + direction, col);
            ChessPosition doublePos = new ChessPosition(row + (2 * direction), col);

            ChessMove doubleMove = new ChessMove(myPosition, doublePos, null);

            if (board.getPiece(singlePos) == null) {
                if (board.getPiece(doublePos) == null) {
                    pawnMoves.add(doubleMove);
                }
            }
        }

        ChessPosition pos = new ChessPosition(row + direction, col);
        // No blocking pieces
        if (board.getPiece(pos) == null) {
            // Promotion
            addMoves(couldPromote, pos);
        }


        if (!rightEdge) {
            ChessPosition foe1 = new ChessPosition(row + direction, col + 1);
            if (board.getPiece(foe1) != null) {
                if (board.getPiece(foe1).getTeamColor() != myTeam) {
                    addMoves(couldPromote, foe1);
                }
            }
        }

        if (!leftEdge) {
            ChessPosition foe2 = new ChessPosition(row + direction, col - 1);
            if (board.getPiece(foe2) != null) {
                if (board.getPiece(foe2).getTeamColor() != myTeam) {
                    addMoves(couldPromote, foe2);
                }
            }
        }
    }

    private void addMoves(boolean couldPromote, ChessPosition pos) {

        if (couldPromote) {
            // Iterate through all possible promotions
            for (ChessPiece.PieceType promotion : ChessPiece.PieceType.values()) {
                if (promotion != PieceType.KING && promotion != PieceType.PAWN) {
                    ChessMove move = new ChessMove(myPosition, pos, promotion);
                    pawnMoves.add(move);
                }
            }
        } else {
            pawnMoves.add(new ChessMove(myPosition, pos, null));
        }
    }
}
