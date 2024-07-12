package chess;

import java.util.ArrayList;

public class KingMoves extends ChessPiece {

    private final int row;
    private final int col;
    private final boolean leftEdge;
    private final boolean rightEdge;
    private final boolean bottomEdge;
    private final boolean topEdge;

    private final ChessBoard board;
    private final ChessPosition myPosition;

    private ArrayList<ChessMove> kingMoves = new ArrayList<>();
    private final ChessGame.TeamColor myTeam;




    public KingMoves(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type,
                     ChessBoard board, ChessPosition myPosition, int col, int row) {
        super(pieceColor, type);
        int min = 1;
        int max = 8;
        leftEdge = (col == min);
        rightEdge = (col == max);
        bottomEdge = (row == min);
        topEdge = (row == max);
        this.col = col;
        this.row = row;
        this.board = board;
        this.myPosition = myPosition;
        myTeam = board.getPiece(myPosition).getTeamColor();
        kingMoves = kingMoves();
    }

    public ArrayList<ChessMove> getKingMoves() {
        return kingMoves;
    }

    private ArrayList<ChessMove> kingMoves() {
        // Add down moves
        if (!bottomEdge) {
            // Down left
            if (!leftEdge) {
                ChessPosition pos = new ChessPosition(row - 1, col - 1);
                ChessMove move = new ChessMove(myPosition, pos, null);
                if (board.getPiece(pos) != null) {
                    if (board.getPiece(pos).getTeamColor() != myTeam) {
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
                    if (board.getPiece(pos).getTeamColor() != myTeam) {
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
                if (board.getPiece(pos).getTeamColor() != myTeam) {
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
                    if (board.getPiece(pos).getTeamColor() != myTeam) {
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
                    if (board.getPiece(pos).getTeamColor() != myTeam) {
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
                if (board.getPiece(pos).getTeamColor() != myTeam) {
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
                if (board.getPiece(pos).getTeamColor() != myTeam) {
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
                if (board.getPiece(pos).getTeamColor() != myTeam) {
                    kingMoves.add(move);
                }
            } else {
                kingMoves.add(move);
            }
        }

        return kingMoves;
    }
}