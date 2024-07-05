package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor team;
    private ChessBoard board;
    private ArrayList<ChessMove> whiteMoves;
    private ArrayList<ChessMove> blackMoves;

    public ChessGame() {
        team = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.team;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team = team;
    }

    /**
     * Calculate all the moves possible for a white and black
     */
    public void updateTeamMoves() {
        ArrayList<ChessMove> whiteMoves = new ArrayList<>();
        ArrayList<ChessMove> blackMoves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece piece = this.board.getPiece(pos);
                if (piece != null) {
                    if (piece.getTeamColor() == TeamColor.WHITE) {
                        whiteMoves.addAll(piece.pieceMoves(this.board, pos));
                    } else {
                        blackMoves.addAll(piece.pieceMoves(this.board, pos));
                    }
                }
            }
        }
        this.whiteMoves = whiteMoves;
        this.blackMoves = blackMoves;
    }

    /**
     * Calculate the position of a team's king
     *
     * @param teamColor the team of the player
     * @return ChessPosition the position of the king
     */
    public ChessPosition getKingPosition(TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece piece = this.board.getPiece(pos);
                if (piece != null) {
                    if (piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING) {
                        return pos;
                    }
                }
            }
        }
        return null;
    }


    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        // Save ChessPosition and ChessPiece
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>) piece.pieceMoves(board, startPosition);

        ChessPiece tempPiece;
        ArrayList<ChessMove> movesToRemove = new ArrayList<>();
        for (ChessMove move : validMoves) {
            ChessPosition endPos = move.getEndPosition();
            ChessPiece capturePiece = board.getPiece(endPos);

            // Change the piece to its new promotion
            if (move.getPromotionPiece() != null) {
                tempPiece = new ChessPiece(team, move.getPromotionPiece());
            } else {
                tempPiece = piece;
            }

            board.addPiece(move.getEndPosition(), tempPiece);
            board.removePiece(move.getStartPosition());


            // check if move leaves team in check
            if (isInCheck(piece.getTeamColor())) {
                movesToRemove.add(move);
            }
            board.removePiece(move.getEndPosition());
            board.addPiece(move.getStartPosition(), piece);
            if (capturePiece != null) {
                board.addPiece(endPos, capturePiece);
            }
        }
        validMoves.removeAll(movesToRemove);
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        // Save ChessPosition and ChessPiece
        ChessPosition startPos = move.getStartPosition();
        ChessPiece piece = board.getPiece(startPos);
        if (piece == null) {
            throw new InvalidMoveException();
        }


        board.addPiece(move.getEndPosition(), piece);
        board.removePiece(move.getStartPosition());


    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        updateTeamMoves();
        ArrayList<ChessMove> foeMoves = (teamColor == TeamColor.WHITE) ? blackMoves : whiteMoves;
        ChessPosition kingPos = getKingPosition(teamColor);
        for (ChessMove move : foeMoves) {
            if (move.getEndPosition().equals(kingPos)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }


    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE, BLACK
    }
}
