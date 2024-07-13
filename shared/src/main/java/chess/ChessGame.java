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
        // Initialize arrays for the team moves
        ArrayList<ChessMove> whiteMoves = new ArrayList<>();
        ArrayList<ChessMove> blackMoves = new ArrayList<>();
        // Iterate over all squares
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece piece = this.board.getPiece(pos);
                // Check if square is null
                if (piece != null) {
                    // Add moves to whiteMoves and blackMoves
                    if (piece.getTeamColor() == TeamColor.WHITE) {
                        whiteMoves.addAll(piece.pieceMoves(this.board, pos));
                    } else {
                        blackMoves.addAll(piece.pieceMoves(this.board, pos));
                    }
                }
            }
        }
        // Save to class fields
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
        // Iterate over all squares
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece piece = this.board.getPiece(pos);
                // Check if square is null
                if (piece != null) {
                    // Check if piece is the team's king
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

        // Initialize array of ChessMoves, ChessPiece, and movesToRemove
        ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>) piece.pieceMoves(board, startPosition);
        ChessPiece tempPiece;
        ArrayList<ChessMove> movesToRemove = new ArrayList<>();

        // Iterate over all valid moves
        for (ChessMove move : validMoves) {
            ChessPosition endPos = move.endPosition();
            ChessPiece capturePiece = board.getPiece(endPos);

            // Change the piece to its new promotion
            if (move.promotionPiece() != null) {
                tempPiece = new ChessPiece(team, move.promotionPiece());
            } else {
                tempPiece = piece;
            }

            board.addPiece(move.endPosition(), tempPiece);
            board.removePiece(move.startPosition());


            // Check if move leaves team in check
            if (isInCheck(piece.getTeamColor())) {
                movesToRemove.add(move);
            }
            board.removePiece(move.endPosition());
            board.addPiece(move.startPosition(), piece);

            // Replace any pieces captured
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
        ChessPosition startPos = move.startPosition();
        ChessPiece piece = board.getPiece(startPos);
        if (piece == null) {
            throw new InvalidMoveException();
        }

        // Check if it is the corresponding team's turn
        if (piece.getTeamColor() != team) {
            throw new InvalidMoveException();
        }
        ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>) validMoves(startPos);

        // Check if the chess piece can move there
        if (!validMoves.contains(move)) {
            throw new InvalidMoveException();
        }
        if (move.promotionPiece() != null) {
            piece = new ChessPiece(team, move.promotionPiece());
        }

        // Add the new piece and remove the old piece
        board.addPiece(move.endPosition(), piece);
        board.removePiece(move.startPosition());

        team = (team == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        // Update list of possible moves
        updateTeamMoves();

        // Initialize the moves the foes can make
        ArrayList<ChessMove> foeMoves = (teamColor == TeamColor.WHITE) ? blackMoves : whiteMoves;

        // Save the position of team's king
        ChessPosition kingPos = getKingPosition(teamColor);

        // Iterate over foe moves
        for (ChessMove move : foeMoves) {

            // Check if any moves can hit the king
            if (move.endPosition().equals(kingPos)) {
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

        // In Checkmate if in check and there are no valid moves
        return isInCheck(teamColor) && hasValidMoves(teamColor);
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        // In Stalemate if not in check and there are no valid moves
        return !isInCheck(teamColor) && hasValidMoves(teamColor);
    }

    /**
     * Determines if there are no valid moves
     *
     * @param teamColor which team to check for valid moves
     * @return True if the specified team has valid moves, otherwise false
     */
    public boolean hasValidMoves(TeamColor teamColor) {

        // Initialize array of ChessMoves
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        // Iterate over all squares
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece piece = this.board.getPiece(pos);

                // Verify square has a piece
                if (piece != null) {

                    // Add valid moves to the array
                    if (piece.getTeamColor() == teamColor) {
                        validMoves.addAll(validMoves(pos));
                    }
                }
            }
        }
        return validMoves.isEmpty();
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

