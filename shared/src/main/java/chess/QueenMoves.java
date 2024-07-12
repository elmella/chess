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

    private boolean upBlocked = false;
    private boolean upRightBlocked = false;
    private boolean rightBlocked = false;
    private boolean downRightBlocked = false;
    private boolean downBlocked = false;
    private boolean downLeftBlocked = false;
    private boolean leftBlocked = false;
    private boolean upLeftBlocked = false;

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

            leftEdge = (col - i < min);
            rightEdge = (col + i > max);

            // Up left
            if (!leftEdge && !upLeftBlocked) {
                ChessPosition pos = new ChessPosition(row + i, col - i);
                ChessMove move = new ChessMove(myPosition, pos, null);
                if (board.getPiece(pos) != null) {
                    upLeftBlocked = true;
                    if (board.getPiece(pos).getTeamColor() != myTeam) {
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
                    if (board.getPiece(pos).getTeamColor() != myTeam) {
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
                    if (board.getPiece(pos).getTeamColor() != myTeam) {
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
                    if (board.getPiece(pos).getTeamColor() != myTeam) {
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
                    if (board.getPiece(pos).getTeamColor() != myTeam) {
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
                    if (board.getPiece(pos).getTeamColor() != myTeam) {
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
                    if (board.getPiece(pos).getTeamColor() != myTeam) {
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
                    if (board.getPiece(pos).getTeamColor() != myTeam) {
                        queenMoves.add(move);
                    }
                } else {
                    queenMoves.add(move);
                }
            }
        }
        return queenMoves;
    }

    private void addMoves(BooleanWrapper blocked, int row, int col) {
        if (!blocked.value) {
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
}
