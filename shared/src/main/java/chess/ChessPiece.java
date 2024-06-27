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
        return "ChessPiece{" + "pieceColor=" + pieceColor + ", type=" + type + '}';
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

        boolean rightUpBlocked = false;
        boolean leftUpBlocked = false;
        boolean upBlocked = false;
        boolean rightDownBlocked = false;
        boolean leftDownBlocked = false;
        boolean downBlocked = false;
        boolean rightBlocked = false;
        boolean leftBlocked = false;

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

                for (int i = 1; i <= max - row; i++) {
                    // Right up diagonal
                    if (col + i <= max && !rightUpBlocked) {
                        ChessPosition pos = new ChessPosition(row + i, col + i);
                        if (board.getPiece(pos) != null) {
                            rightUpBlocked = true;
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                queenMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            queenMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                    // Left up diagonal
                    if (col - i >= min && !leftUpBlocked) {
                        ChessPosition pos = new ChessPosition(row + i, col - i);

                        if (board.getPiece(pos) != null) {
                            leftUpBlocked = true;
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                queenMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            queenMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                    // Up
                    if (!upBlocked) {
                        ChessPosition pos = new ChessPosition(row + i, col);

                        if (board.getPiece(pos) != null) {
                            upBlocked = true;
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                queenMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            queenMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                }

                // Move down
                for (int i = 1; i <= row - min; i++) {
                    // Right down diagonal
                    if (col + i <= max && !rightDownBlocked) {
                        ChessPosition pos = new ChessPosition(row - i, col + i);
                        if (board.getPiece(pos) != null) {
                            rightDownBlocked = true;
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                queenMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            queenMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                    // Left up diagonal
                    if (col - i >= min && !leftDownBlocked) {
                        ChessPosition pos = new ChessPosition(row - i, col - i);

                        if (board.getPiece(pos) != null) {
                            leftDownBlocked = true;
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                queenMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            queenMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                    // Down
                    if (!downBlocked) {
                        ChessPosition pos = new ChessPosition(row - i, col);

                        if (board.getPiece(pos) != null) {
                            downBlocked = true;
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                queenMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            queenMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                }

                // Right
                for (int i = 1; i <= max - col; i++) {
                    if (!rightBlocked) {
                        ChessPosition pos = new ChessPosition(row, col + i);
                        if (board.getPiece(pos) != null) {
                            rightBlocked = true;
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                queenMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            queenMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                }

                // Left
                for (int i = 1; i <= col - min; i++) {
                    if (!leftBlocked) {
                        ChessPosition pos = new ChessPosition(row, col - i);
                        if (board.getPiece(pos) != null) {
                            leftBlocked = true;
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                queenMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            queenMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                }

                validMoves.addAll(queenMoves);
                break;

            case ROOK:
                ArrayList<ChessMove> rookMoves = new ArrayList<>();

                for (int i = 1; i <= max - row; i++) {
                    // Up
                    if (!upBlocked) {
                        ChessPosition pos = new ChessPosition(row + i, col);

                        if (board.getPiece(pos) != null) {
                            upBlocked = true;
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                rookMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            rookMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                }

                // Down
                for (int i = 1; i <= row - min; i++) {
                    if (!downBlocked) {
                        ChessPosition pos = new ChessPosition(row - i, col);

                        if (board.getPiece(pos) != null) {
                            downBlocked = true;
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                rookMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            rookMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                }

                // Right
                for (int i = 1; i <= max - col; i++) {
                    if (!rightBlocked) {
                        ChessPosition pos = new ChessPosition(row, col + i);
                        if (board.getPiece(pos) != null) {
                            rightBlocked = true;
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                rookMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            rookMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                }

                // Left
                for (int i = 1; i <= col - min; i++) {
                    if (!leftBlocked) {
                        ChessPosition pos = new ChessPosition(row, col - i);
                        if (board.getPiece(pos) != null) {
                            leftBlocked = true;
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                rookMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            rookMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                }

                validMoves.addAll(rookMoves);
                break;

            case KNIGHT:
                ArrayList<ChessMove> knightMoves = new ArrayList<>();

                // Up 2, right 1
                if (row + 2 <= max) {
                    if (col + 1 <= max) {
                        ChessPosition pos = new ChessPosition(row + 2, col + 1);
                        if (board.getPiece(pos) != null) {
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                knightMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            knightMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                }

                // Up 2, left 1
                if (row + 2 <= max) {
                    if (col - 1 >= min) {
                        ChessPosition pos = new ChessPosition(row + 2, col - 1);
                        if (board.getPiece(pos) != null) {
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                knightMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            knightMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                }

                // Up 1, right 2
                if (row + 1 <= max) {
                    if (col + 2 <= max) {
                        ChessPosition pos = new ChessPosition(row + 1, col + 2);
                        if (board.getPiece(pos) != null) {
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                knightMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            knightMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                }

                // Up 1, left 2
                if (row + 1 <= max) {
                    if (col - 2 >= min) {
                        ChessPosition pos = new ChessPosition(row + 1, col - 2);
                        if (board.getPiece(pos) != null) {
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                knightMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            knightMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                }

                // Down 2, right 1
                if (row - 2 >= min) {
                    if (col + 1 <= max) {
                        ChessPosition pos = new ChessPosition(row - 2, col + 1);
                        if (board.getPiece(pos) != null) {
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                knightMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            knightMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                }

                // Up 2, left 1
                if (row - 2 >= min) {
                    if (col - 1 >= min) {
                        ChessPosition pos = new ChessPosition(row - 2, col - 1);
                        if (board.getPiece(pos) != null) {
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                knightMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            knightMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                }

                // Up 1, right 2
                if (row - 1 >= min) {
                    if (col + 2 <= max) {
                        ChessPosition pos = new ChessPosition(row - 1, col + 2);
                        if (board.getPiece(pos) != null) {
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                knightMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            knightMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                }

                // Up 1, left 2
                if (row - 1 >= min) {
                    if (col - 2 >= min) {
                        ChessPosition pos = new ChessPosition(row - 1, col - 2);
                        if (board.getPiece(pos) != null) {
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                knightMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            knightMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                }

                validMoves.addAll(knightMoves);
                break;

            case BISHOP:
                ArrayList<ChessMove> bishopMoves = new ArrayList<>();

                for (int i = 1; i <= max - row; i++) {
                    // Right up diagonal
                    if (col + i <= max && !rightUpBlocked) {
                        ChessPosition pos = new ChessPosition(row + i, col + i);
                        if (board.getPiece(pos) != null) {
                            rightUpBlocked = true;
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                bishopMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            bishopMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                    // Left up diagonal
                    if (col - i >= min && !leftUpBlocked) {
                        ChessPosition pos = new ChessPosition(row + i, col - i);

                        if (board.getPiece(pos) != null) {
                            leftUpBlocked = true;
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                bishopMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            bishopMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                }

                // Move down
                for (int i = 1; i <= row - min; i++) {
                    // Right down diagonal
                    if (col + i <= max && !rightDownBlocked) {
                        ChessPosition pos = new ChessPosition(row - i, col + i);
                        if (board.getPiece(pos) != null) {
                            rightDownBlocked = true;
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                bishopMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            bishopMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                    // Left up diagonal
                    if (col - i >= min && !leftDownBlocked) {
                        ChessPosition pos = new ChessPosition(row - i, col - i);

                        if (board.getPiece(pos) != null) {
                            leftDownBlocked = true;
                            if (board.getPiece(pos).pieceColor != pieceColor)
                                bishopMoves.add(new ChessMove(myPosition, pos, null));
                        } else {
                            bishopMoves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                }

                validMoves.addAll(bishopMoves);
                break;

            case PAWN:
                ArrayList<ChessMove> pawnMoves = new ArrayList<>();
                int direction;
                int startRow;
                int endRow;

                if (pieceColor == ChessGame.TeamColor.WHITE) {
                    direction = 1;
                    startRow = 2;
                    endRow = 8;
                }
                else {
                    direction = -1;
                    startRow = 7;
                    endRow = 1;
                }

                // Double advancement
                if (row == startRow) {
                    ChessPosition pos = new ChessPosition(row + (2 * direction), col);

                    // Check double square is open
                    if (board.getPiece(pos) == null) {
                        pawnMoves.add(new ChessMove(myPosition, pos, null));
                    }
                }

                // Standard advancement
                ChessPosition pos = new ChessPosition(row + direction, col);

                // Promotion
                if (row + 1 == endRow) {
                    if (board.getPiece(pos) == null) {
                        for (ChessPiece.PieceType promotion : ChessPiece.PieceType.values()) {
                            if (promotion != ChessPiece.PieceType.PAWN && promotion != ChessPiece.PieceType.KING) {
                                pawnMoves.add(new ChessMove(myPosition, pos, promotion));
                            }
                        }
                    }
                } else {
                    if (board.getPiece(pos) == null) {
                        pawnMoves.add(new ChessMove(myPosition, pos, null));
                    }
                }

                // Right diagonal attack
                if (col + 1 <= max) {
                    ChessPosition foeRight = new ChessPosition(row + direction, col + 1);
                    // Check for piece on right
                    if (board.getPiece(foeRight) != null) {
                        // Check piece is foe
                        if (board.getPiece(foeRight).pieceColor != pieceColor)
                            // Check for promotion
                            if (row + 1 == endRow) {
                                for (ChessPiece.PieceType promotion : ChessPiece.PieceType.values()) {
                                    if (promotion != ChessPiece.PieceType.PAWN && promotion != ChessPiece.PieceType.KING) {
                                        pawnMoves.add(new ChessMove(myPosition, pos, promotion));
                                    }
                                }
                            } else {
                                pawnMoves.add(new ChessMove(myPosition, foeRight, null));
                            }
                    }
                }

                // Left diagonal attack
                if (col - 1 >= min) {
                    ChessPosition foeRight = new ChessPosition(row + direction, col - 1);
                    // Check for piece on right
                    if (board.getPiece(foeRight) != null) {
                        // Check piece is foe
                        if (board.getPiece(foeRight).pieceColor != pieceColor)
                            // Check for promotion
                            if (row + 1 == endRow) {
                                for (ChessPiece.PieceType promotion : ChessPiece.PieceType.values()) {
                                    if (promotion != ChessPiece.PieceType.PAWN && promotion != ChessPiece.PieceType.KING) {
                                        pawnMoves.add(new ChessMove(myPosition, pos, promotion));
                                    }
                                }
                            } else {
                                pawnMoves.add(new ChessMove(myPosition, foeRight, null));
                            }
                    }
                }

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
