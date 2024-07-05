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
        if (this == o) return true;
        if (!(o instanceof ChessPiece that)) return false;
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
        int max = 8;
        int min = 1;
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        boolean upBlocked = false;
        boolean upRightBlocked = false;
        boolean rightBlocked = false;
        boolean downRightBlocked = false;
        boolean downBlocked = false;
        boolean downLeftBlocked = false;
        boolean leftBlocked = false;
        boolean upLeftBlocked = false;

        boolean leftEdge = (col == min);
        boolean rightEdge = (col == max);
        boolean bottomEdge = (row == min);
        boolean topEdge = (row == max);

        ChessGame.TeamColor myTeam = board.getPiece(myPosition).getTeamColor();

        switch (type) {

            case KING:
                ArrayList<ChessMove> kingMoves = new ArrayList<>();
                // Add down moves
                if (!bottomEdge) {
                    // Down left
                    if (!leftEdge) {
                        ChessPosition pos = new ChessPosition(row - 1, col - 1);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                kingMoves.add(move);
                            }
                        } else {
                            kingMoves.add(move);
                        }
                        // Down right
                    }
                    if (!rightEdge) {
                        ChessPosition pos = new ChessPosition(row - 1, col + 1);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                kingMoves.add(move);
                            }
                        } else {
                            kingMoves.add(move);
                        }
                    }

                    // Down
                    ChessPosition pos = new ChessPosition(row - 1, col);
                    ChessMove move = new ChessMove(myPosition, pos, null);
                    if (board.getPiece(pos) != null) {
                        if (board.getPiece(pos).pieceColor != myTeam) {
                            kingMoves.add(move);
                        }
                    } else {
                        kingMoves.add(move);
                    }
                }

                // Add up moves
                if (!topEdge) {
                    // Up left
                    if (!leftEdge) {
                        ChessPosition pos = new ChessPosition(row + 1, col - 1);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                kingMoves.add(move);
                            }
                        } else {
                            kingMoves.add(move);
                        }
                        // up right
                    }
                    if (!rightEdge) {
                        ChessPosition pos = new ChessPosition(row + 1, col + 1);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                kingMoves.add(move);
                            }
                        } else {
                            kingMoves.add(move);
                        }
                    }

                    // Up
                    ChessPosition pos = new ChessPosition(row + 1, col);
                    ChessMove move = new ChessMove(myPosition, pos, null);
                    if (board.getPiece(pos) != null) {
                        if (board.getPiece(pos).pieceColor != myTeam) {
                            kingMoves.add(move);
                        }
                    } else {
                        kingMoves.add(move);
                    }
                }

                // Right
                if (!rightEdge) {
                    ChessPosition pos = new ChessPosition(row, col + 1);
                    ChessMove move = new ChessMove(myPosition, pos, null);
                    if (board.getPiece(pos) != null) {
                        if (board.getPiece(pos).pieceColor != myTeam) {
                            kingMoves.add(move);
                        }
                    } else {
                        kingMoves.add(move);
                    }
                }

                // Left
                if (!leftEdge) {
                    ChessPosition pos = new ChessPosition(row, col - 1);
                    ChessMove move = new ChessMove(myPosition, pos, null);
                    if (board.getPiece(pos) != null) {
                        if (board.getPiece(pos).pieceColor != myTeam) {
                            kingMoves.add(move);
                        }
                    } else {
                        kingMoves.add(move);
                    }
                }

                validMoves.addAll(kingMoves);

                break;

            case QUEEN:
                ArrayList<ChessMove> queenMoves = new ArrayList<>();
                // Add up moves
                for (int i = 1; i <= max - row; i++) {

                    leftEdge = (col - i < min);
                    rightEdge = (col + i > max);

                    // Up left
                    if (!leftEdge && !upLeftBlocked) {
                        ChessPosition pos = new ChessPosition(row + i, col - i);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            upLeftBlocked = true;
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                queenMoves.add(move);
                            }
                        } else {
                            queenMoves.add(move);
                        }
                    }
                    // Up right
                    if (!rightEdge && !upRightBlocked) {
                        ChessPosition pos = new ChessPosition(row + i, col + i);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            upRightBlocked = true;
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                queenMoves.add(move);
                            }
                        } else {
                            queenMoves.add(move);
                        }
                    }

                    // Up
                    if (!upBlocked) {
                        ChessPosition pos = new ChessPosition(row + i, col);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            upBlocked = true;
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                queenMoves.add(move);
                            }
                        } else {
                            queenMoves.add(move);
                        }
                    }
                }

                // Add down moves
                for (int i = 1; i <= row - min; i++) {
                    leftEdge = (col - i < min);
                    rightEdge = (col + i > max);

                    // Down left
                    if (!leftEdge && !downLeftBlocked) {
                        ChessPosition pos = new ChessPosition(row - i, col - i);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            downLeftBlocked = true;
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                queenMoves.add(move);
                            }
                        } else {
                            queenMoves.add(move);
                        }
                    }
                    // Down right
                    if (!rightEdge && !downRightBlocked) {
                        ChessPosition pos = new ChessPosition(row - i, col + i);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            downRightBlocked = true;
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                queenMoves.add(move);
                            }
                        } else {
                            queenMoves.add(move);
                        }
                    }

                    // Down
                    if (!downBlocked) {
                        ChessPosition pos = new ChessPosition(row - i, col);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            downBlocked = true;
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                queenMoves.add(move);
                            }
                        } else {
                            queenMoves.add(move);
                        }
                    }
                }

                // Right
                for (int i = 1; i <= max - col; i++) {
                    if (!rightBlocked) {
                        ChessPosition pos = new ChessPosition(row, col + i);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            rightBlocked = true;
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                queenMoves.add(move);
                            }
                        } else {
                            queenMoves.add(move);
                        }
                    }
                }

                // Left
                for (int i = 1; i <= col - min; i++) {
                    if (!leftBlocked) {
                        ChessPosition pos = new ChessPosition(row, col - i);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            leftBlocked = true;
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                queenMoves.add(move);
                            }
                        } else {
                            queenMoves.add(move);
                        }
                    }
                }

                validMoves.addAll(queenMoves);

                break;
            case ROOK:
                ArrayList<ChessMove> rookMoves = new ArrayList<>();
                // Add up moves
                for (int i = 1; i <= max - row; i++) {

                    // Up
                    if (!upBlocked) {
                        ChessPosition pos = new ChessPosition(row + i, col);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            upBlocked = true;
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                rookMoves.add(move);
                            }
                        } else {
                            rookMoves.add(move);
                        }
                    }
                }

                // Add down moves
                for (int i = 1; i <= row - min; i++) {
                    if (!downBlocked) {
                        ChessPosition pos = new ChessPosition(row - i, col);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            downBlocked = true;
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                rookMoves.add(move);
                            }
                        } else {
                            rookMoves.add(move);
                        }
                    }
                }

                // Right
                for (int i = 1; i <= max - col; i++) {
                    if (!rightBlocked) {
                        ChessPosition pos = new ChessPosition(row, col + i);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            rightBlocked = true;
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                rookMoves.add(move);
                            }
                        } else {
                            rookMoves.add(move);
                        }
                    }
                }

                // Left
                for (int i = 1; i <= col - min; i++) {
                    if (!leftBlocked) {
                        ChessPosition pos = new ChessPosition(row, col - i);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            leftBlocked = true;
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                rookMoves.add(move);
                            }
                        } else {
                            rookMoves.add(move);
                        }
                    }
                }

                validMoves.addAll(rookMoves);
                break;
            case BISHOP:
                ArrayList<ChessMove> bishopMoves = new ArrayList<>();
                // Add up moves
                for (int i = 1; i <= max - row; i++) {

                    leftEdge = (col - i < min);
                    rightEdge = (col + i > max);

                    // Up left
                    if (!leftEdge && !upLeftBlocked) {
                        ChessPosition pos = new ChessPosition(row + i, col - i);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            upLeftBlocked = true;
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                bishopMoves.add(move);
                            }
                        } else {
                            bishopMoves.add(move);
                        }
                    }
                    // Up right
                    if (!rightEdge && !upRightBlocked) {
                        ChessPosition pos = new ChessPosition(row + i, col + i);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            upRightBlocked = true;
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                bishopMoves.add(move);
                            }
                        } else {
                            bishopMoves.add(move);
                        }
                    }
                }

                // Add down moves
                for (int i = 1; i <= row - min; i++) {
                    leftEdge = (col - i < min);
                    rightEdge = (col + i > max);

                    // Down left
                    if (!leftEdge && !downLeftBlocked) {
                        ChessPosition pos = new ChessPosition(row - i, col - i);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            downLeftBlocked = true;
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                bishopMoves.add(move);
                            }
                        } else {
                            bishopMoves.add(move);
                        }
                    }
                    // Down right
                    if (!rightEdge && !downRightBlocked) {
                        ChessPosition pos = new ChessPosition(row - i, col + i);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            downRightBlocked = true;
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                bishopMoves.add(move);
                            }
                        } else {
                            bishopMoves.add(move);
                        }
                    }
                }
                validMoves.addAll(bishopMoves);

                break;
            case KNIGHT:
                ArrayList<ChessMove> knightMoves = new ArrayList<>();

                // 2+ below max
                if (row < max - 1) {

                    // 2 up, 1 right
                    if (!rightEdge) {
                        ChessPosition pos = new ChessPosition(row + 2, col + 1);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                knightMoves.add(move);
                            }
                        } else {
                            knightMoves.add(move);
                        }
                    }

                    // 2 up, 1 left
                    if (!leftEdge) {
                        ChessPosition pos = new ChessPosition(row + 2, col - 1);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                knightMoves.add(move);
                            }
                        } else {
                            knightMoves.add(move);
                        }
                    }
                }

                // 2+ above min
                if (row > min + 1) {

                    // 2 down, 1 right
                    if (!rightEdge) {
                        ChessPosition pos = new ChessPosition(row - 2, col + 1);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                knightMoves.add(move);
                            }
                        } else {
                            knightMoves.add(move);
                        }
                    }

                    // 2 down, 1 left
                    if (!leftEdge) {
                        ChessPosition pos = new ChessPosition(row - 2, col - 1);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                knightMoves.add(move);
                            }
                        } else {
                            knightMoves.add(move);
                        }
                    }
                }

                // 2+ left max
                if (col < max - 1) {

                    // 1 up, 2 right
                    if (!topEdge) {
                        ChessPosition pos = new ChessPosition(row + 1, col + 2);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                knightMoves.add(move);
                            }
                        } else {
                            knightMoves.add(move);
                        }
                    }

                    // 1 down, 2 right
                    if (!bottomEdge) {
                        ChessPosition pos = new ChessPosition(row - 1, col + 2);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                knightMoves.add(move);
                            }
                        } else {
                            knightMoves.add(move);
                        }
                    }
                }

                // 2+ right min
                if (col > min + 1) {

                    // 1 up, 2 left
                    if (!topEdge) {
                        ChessPosition pos = new ChessPosition(row + 1, col - 2);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                knightMoves.add(move);
                            }
                        } else {
                            knightMoves.add(move);
                        }
                    }

                    // 1 down, 2 left
                    if (!bottomEdge) {
                        ChessPosition pos = new ChessPosition(row - 1, col - 2);
                        ChessMove move = new ChessMove(myPosition, pos, null);
                        if (board.getPiece(pos) != null) {
                            if (board.getPiece(pos).pieceColor != myTeam) {
                                knightMoves.add(move);
                            }
                        } else {
                            knightMoves.add(move);
                        }
                    }
                }

                validMoves.addAll(knightMoves);

                break;
            case PAWN:
                ArrayList<ChessMove> pawnMoves = new ArrayList<>();

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


                if (!rightEdge) {
                    ChessPosition foe1 = new ChessPosition(row + direction, col + 1);
                    if (board.getPiece(foe1) != null) {
                        if (board.getPiece(foe1).pieceColor != myTeam) {
                            if (couldPromote) {
                                // Iterate through all possible promotions
                                for (ChessPiece.PieceType promotion : ChessPiece.PieceType.values()) {
                                    if (promotion != PieceType.KING && promotion != PieceType.PAWN) {
                                        ChessMove move = new ChessMove(myPosition, foe1, promotion);
                                        pawnMoves.add(move);
                                    }
                                }
                            } else {
                                pawnMoves.add(new ChessMove(myPosition, foe1, null));
                            }
                        }
                    }
                }

                if (!leftEdge) {
                    ChessPosition foe2 = new ChessPosition(row + direction, col - 1);
                    if (board.getPiece(foe2) != null) {
                        if (board.getPiece(foe2).pieceColor != myTeam) {
                            if (couldPromote) {
                                // Iterate through all possible promotions
                                for (ChessPiece.PieceType promotion : ChessPiece.PieceType.values()) {
                                    if (promotion != PieceType.KING && promotion != PieceType.PAWN) {
                                        ChessMove move = new ChessMove(myPosition, foe2, promotion);
                                        pawnMoves.add(move);
                                    }
                                }
                            } else {
                                pawnMoves.add(new ChessMove(myPosition, foe2, null));
                            }
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