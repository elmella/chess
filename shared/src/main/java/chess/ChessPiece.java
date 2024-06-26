package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPiece that)) return false;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int min = 1;
        int max = 8;

        ArrayList<ChessMove> validMoves = new ArrayList<>();

        switch (type) {
            case KING:
                ArrayList<ChessMove> kingMoves = new ArrayList<>();
                if (row < max) {
                    if (col < max) {
                        // Right up
                        kingMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 1), null));
                        // Right
                        kingMoves.add(new ChessMove(myPosition, new ChessPosition(row, col + 1), null));
                    }
                    // Up
                    kingMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), null));
                    if (col > min) {
                        // Left up
                        kingMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 1), null));
                        // Left
                        kingMoves.add(new ChessMove(myPosition, new ChessPosition(row, col - 1), null));
                    }
                }

                if (row > min) {
                    if (col < max) {
                        // Right down
                        kingMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 1), null));
                        // Right
                        kingMoves.add(new ChessMove(myPosition, new ChessPosition(row, col + 1), null));
                    }
                    // Down
                    kingMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), null));
                    if (col > min) {
                        // Left Down
                        kingMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 1), null));
                        // Left
                        kingMoves.add(new ChessMove(myPosition, new ChessPosition(row, col - 1), null));
                    }
                }

                // Check for same team blocking each move
                kingMoves.removeIf(move -> {
                    ChessPiece piece = board.getPiece(move.getEndPosition());
                    return piece != null && piece.pieceColor == pieceColor;
                });

                validMoves.addAll(kingMoves);

                break;
            case QUEEN:
                ArrayList<ChessMove> queenMoves = new ArrayList<>();
                // Move up
                for (int i = 1; i <= max - row; i++) {
                    if (col + i < max) {
                        // Right up diagonal
                        queenMoves.add(new ChessMove(myPosition, new ChessPosition(row + i, col + i), null));
                    }
                    if (col - i > min) {
                        // Left up diagonal
                        queenMoves.add(new ChessMove(myPosition, new ChessPosition(row + i, col - i), null));
                    }
                    // Up
                    queenMoves.add(new ChessMove(myPosition, new ChessPosition(row + i, col - i), null));
                }

                // Check for same team blocking each move
                queenMoves.removeIf(move -> {
                    ChessPiece piece = board.getPiece(move.getEndPosition());
                    return piece != null && piece.pieceColor == pieceColor;
                });

                validMoves.addAll(queenMoves);

                break;
            case ROOK:
                System.out.println("ROOK");
                break;
            case KNIGHT:
                System.out.println("KNIGHT");
                break;
            case BISHOP:
                System.out.println("BISHOP");
                break;
            case PAWN:
                System.out.println("PAWN");
                break;
        }
        return validMoves;
    }
}
