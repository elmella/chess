package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static ui.EscapeSequences.*;

public class DrawBoard {

    private static final Map<ChessPiece.PieceType, String> WHITE_PIECE_MAP = Map.of(
            ChessPiece.PieceType.KING, " K ",
            ChessPiece.PieceType.QUEEN, " Q ",
            ChessPiece.PieceType.BISHOP, " B ",
            ChessPiece.PieceType.KNIGHT, " N ",
            ChessPiece.PieceType.ROOK, " R ",
            ChessPiece.PieceType.PAWN, " P "
    );

    private static final Map<ChessPiece.PieceType, String> BLACK_PIECE_MAP = Map.of(
            ChessPiece.PieceType.KING, " k ",
            ChessPiece.PieceType.QUEEN, " q ",
            ChessPiece.PieceType.BISHOP, " b ",
            ChessPiece.PieceType.KNIGHT, " n ",
            ChessPiece.PieceType.ROOK, " r ",
            ChessPiece.PieceType.PAWN, " p "
    );

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    // Padded characters.
    private static final String EMPTY = "   ";
    private static final String X = " X ";
    private static final String O = " O ";

    private static Random rand = new Random();


    public static void drawHeaders(PrintStream out) {

        setBlack(out);

        String[] headers = { " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h " };
        printHeaderText(out, " ");

        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);

            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
            }

        }
        printHeaderText(out, " ");

        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(player);

        setBlack(out);
    }



    public static void drawChessBoard(PrintStream out, ChessBoard board) {

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {

            drawRowOfSquares(out, boardRow, board);

            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                // Draw horizontal row separator.
                drawHorizontalLine(out);
                setBlack(out);
            }
        }
    }

    private static void drawRowOfSquares(PrintStream out, int boardRow, ChessBoard board) {

        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                printHeaderText(out, String.valueOf(BOARD_SIZE_IN_SQUARES - boardRow));
            } else {
                printHeaderText(out, " ");

            }

            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                setWhite(out);


                if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

                    out.print(EMPTY.repeat(prefixLength));

                    // draw piece
                    System.out.println(boardRow);
                    System.out.println(boardCol);
                    ChessPiece piece = board.getPiece(new ChessPosition(boardRow, boardCol));
                    if (piece != null) {
                        out.print(printPiece(piece));
                    }
                    else {
                        out.print(EMPTY);
                    }
                    out.print(EMPTY.repeat(suffixLength));
                }
                else {
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                }

                if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                    // Draw vertical column separator.

                    setRed(out);
                    out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
                }

                setBlack(out);
            }

            if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                printHeaderText(out, String.valueOf(BOARD_SIZE_IN_SQUARES - boardRow));
            } else {
                printHeaderText(out, " ");

            }

            out.println();

        }
    }

    private static void drawHorizontalLine(PrintStream out) {

        printHeaderText(out, " ");


        int boardSizeInSpaces = BOARD_SIZE_IN_SQUARES * SQUARE_SIZE_IN_PADDED_CHARS +
                (BOARD_SIZE_IN_SQUARES - 1) * LINE_WIDTH_IN_PADDED_CHARS;

        for (int lineRow = 0; lineRow < LINE_WIDTH_IN_PADDED_CHARS; ++lineRow) {
            setRed(out);
            out.print(EMPTY.repeat(boardSizeInSpaces));

            setBlack(out);
            printHeaderText(out, " ");
            out.println();
        }

    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void printPlayer(PrintStream out, String player) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setWhite(out);
    }

    private static String printPiece(ChessPiece piece) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return WHITE_PIECE_MAP.get(piece.getPieceType());
        } else {
            return BLACK_PIECE_MAP.get(piece.getPieceType());
        }
    }
}