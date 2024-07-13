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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChessPiece that)) {
            return false;
        }
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" + "pieceColor=" + pieceColor + ", type=" + type + '}';
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        switch (type) {

            case KING:
                ArrayList<ChessMove> kingMoves = new KingPiece(pieceColor, type, board, myPosition).getKingMoves();
                validMoves.addAll(kingMoves);
                break;

            case QUEEN:
                ArrayList<ChessMove> queenMoves = new QueenPiece(pieceColor, type, board, myPosition).getQueenMoves();
                validMoves.addAll(queenMoves);
                break;

            case ROOK:
                ArrayList<ChessMove> rookMoves = new RookPiece(pieceColor, type, board, myPosition).getRookMoves();
                validMoves.addAll(rookMoves);
                break;

            case BISHOP:
                ArrayList<ChessMove> bishopMoves = new BishopPiece(pieceColor, type, board, myPosition).getBishopMoves();
                validMoves.addAll(bishopMoves);
                break;

            case KNIGHT:
                ArrayList<ChessMove> knightMoves = new KnightPiece(pieceColor, type, board, myPosition).getKnightMoves();
                validMoves.addAll(knightMoves);
                break;

            case PAWN:
                ArrayList<ChessMove> pawnMoves = new PawnPiece(pieceColor, type, board, myPosition).getPawnMoves();

                validMoves.addAll(pawnMoves);

                break;
        }
        return validMoves;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN
    }
}