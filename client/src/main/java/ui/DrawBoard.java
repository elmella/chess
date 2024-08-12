package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.util.Map;

import static ui.EscapeSequences.*;

public class DrawBoard {

    private static final Map<ChessPiece.PieceType, String> WHITE_PIECE_MAP = Map.of(
            ChessPiece.PieceType.KING, " K ",
            ChessPiece.PieceType.QUEEN, " Q ",
            ChessPiece.PieceType.BISHOP, " B ",
            ChessPiece.PieceType.KNIGHT, " N ",
            ChessPiece.PieceType.ROOK, " R ",
            ChessPiece.PieceType.PAWN, " P ");

    private static final Map<ChessPiece.PieceType, String> BLACK_PIECE_MAP = Map.of(
            ChessPiece.PieceType.KING, " k ",
            ChessPiece.PieceType.QUEEN, " q ",
            ChessPiece.PieceType.BISHOP, " b ",
            ChessPiece.PieceType.KNIGHT, " n ",
            ChessPiece.PieceType.ROOK, " r ",
            ChessPiece.PieceType.PAWN, " p ");

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    // Padded characters.
    private static final String EMPTY = "   ";


    public static void drawHeaders(PrintStream out, boolean reverse) {
        setBlack(out);

        String[] headers = {" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
        printHeaderText(out, " ");

        if (reverse) {
            for (int boardCol = BOARD_SIZE_IN_SQUARES - 1; boardCol >= 0; --boardCol) {
                drawHeader(out, headers[boardCol]);

                if (boardCol > 0) {
                    out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
                }
            }
        } else {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                drawHeader(out, headers[boardCol]);

                if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                    out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
                }
            }
        }

        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;
        out.print(EMPTY.repeat(suffixLength));

        printHeaderText(out, " ");

        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
//        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(player);

        setBlack(out);
    }

    public static void drawChessBoard(PrintStream out, ChessBoard board, boolean reverse) {
        drawHeaders(out, reverse);

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            drawRowOfSquares(out, boardRow, board, reverse);

            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                setBlack(out);
            }
        }

        drawHeaders(out, reverse);
    }

    private static void drawRowOfSquares(PrintStream out, int boardRow, ChessBoard board, boolean reverse) {
        String intChar = String.valueOf(reverse ? boardRow + 1 : BOARD_SIZE_IN_SQUARES - boardRow);
        ChessPiece piece;

        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                printHeaderText(out, intChar);
            } else {
                printHeaderText(out, " ");
            }

            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                boolean isWhiteSquare = (boardRow + boardCol) % 2 == 0;
                if (isWhiteSquare) {
                    setWhite(out);
                } else {
                    setBlackSquare(out);
                }

                if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

                    out.print(EMPTY.repeat(prefixLength));

                    // Adjust piece retrieval for reverse logic
                    if (reverse) {
                        piece = board.getPiece(new ChessPosition(boardRow + 1, boardCol + 1));
                    } else {
                        piece = board.getPiece(new ChessPosition(BOARD_SIZE_IN_SQUARES - boardRow,
                                BOARD_SIZE_IN_SQUARES - boardCol));
                    }
                    if (piece != null) {
                        printPlayer(out, printPiece(piece), isWhiteSquare);
                    } else {
                        out.print(EMPTY);
                    }
                    out.print(EMPTY.repeat(suffixLength));
                } else {
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                }
            }

            if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                printHeaderText(out, intChar);
            } else {
                printHeaderText(out, " ");
            }

            out.println();
        }
    }


    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setBlackSquare(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }


    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void printPlayer(PrintStream out, String player, boolean isWhiteSquare) {
        if (isWhiteSquare) {
            out.print(SET_TEXT_COLOR_BLACK);
        } else {
            out.print(SET_TEXT_COLOR_WHITE);

        }
        out.print(player);
    }

    private static String printPiece(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return WHITE_PIECE_MAP.get(piece.getPieceType());
        } else {
            return BLACK_PIECE_MAP.get(piece.getPieceType());
        }
    }
}