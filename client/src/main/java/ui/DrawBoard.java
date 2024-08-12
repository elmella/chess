package ui;

import chess.*;

import java.io.PrintStream;
import java.util.ArrayList;
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

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(player);

        setBlack(out);
    }

    public static void drawChessBoard(PrintStream out, ChessBoard board, boolean reverse, ArrayList<ChessPosition> validEnds, ChessPosition startPos) {
        drawHeaders(out, reverse);

//        if (!reverse) {
//            for (int boardRow = BOARD_SIZE_IN_SQUARES - 1; boardRow >= 0; --boardRow) {
//                drawRowOfSquares(out, boardRow, board, reverse, validEnds, startPos);
//
//                if (boardRow > 0) {
//                    setBlack(out);
//                }
//            }
//        } else {
//            for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
//                drawRowOfSquares(out, boardRow, board, reverse, validEnds, startPos);
//
//                if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
//                    setBlack(out);
//                }
//            }
//        }
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            drawRowOfSquares(out, boardRow, board, reverse, validEnds, startPos);

            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                setBlack(out);
            }
        }

        drawHeaders(out, reverse);
    }

    private static void drawRowOfSquares(PrintStream out, int boardRow, ChessBoard board, boolean reverse, ArrayList<ChessPosition> validEnds, ChessPosition startPos) {
        String intChar = String.valueOf(reverse ? boardRow + 1 : BOARD_SIZE_IN_SQUARES - boardRow);
        ChessPiece piece = null;

        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                printHeaderText(out, intChar);
            } else {
                printHeaderText(out, " ");
            }

            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {

                ChessPosition pos;
                // Check if new square is valid square to highlight
                if (reverse) {
                     pos = new ChessPosition(boardRow + 1, BOARD_SIZE_IN_SQUARES - boardCol);
                } else {
                    pos = new ChessPosition(BOARD_SIZE_IN_SQUARES - boardRow,
                            boardCol + 1);
                }
                boolean isStartPos = false;
                boolean isValidSquare = false;
                if (startPos != null) {
                    isStartPos = pos.equals(startPos);
                }
                if (validEnds != null) {
                 isValidSquare = validEnds.contains(pos);
                }

                // Check if is a white square
                boolean isWhiteSquare = (boardRow + boardCol) % 2 == 0;

                // Set the background color based on the conditions
                if (isStartPos) {
                    setOverlayColor(out, isWhiteSquare, true);
                } else if (isValidSquare) {
                    setOverlayColor(out, isWhiteSquare, false);
                } else {
                    if (isWhiteSquare) {
                        setWhiteSquare(out);
                    } else {
                        setBlackSquare(out);
                    }
                }

                if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

                    out.print(EMPTY.repeat(prefixLength));
                    piece = board.getPiece(pos);
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

    private static void setOverlayColor(PrintStream out, boolean isWhiteSquare, boolean isStartPos) {
        if (isStartPos) {
            if (!isWhiteSquare) {
                out.print(SET_BG_COLOR_YELLOW);
                out.print(SET_TEXT_COLOR_BLACK);
            } else {
                out.print(SET_BG_COLOR_BRIGHT_YELLOW);
                out.print(SET_TEXT_COLOR_WHITE);
            }
        } else {
            if (!isWhiteSquare) {
                out.print(SET_BG_COLOR_GREEN);
                out.print(SET_TEXT_COLOR_BLACK);
            } else {
                out.print(SET_BG_COLOR_BRIGHT_GREEN);
                out.print(SET_TEXT_COLOR_WHITE);
            }
        }
    }


    private static void setWhiteSquare(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setBlackSquare(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setGreenSquare(PrintStream out) {
        out.print(SET_BG_COLOR_GREEN);
    }

    private static void setYellowSquare(PrintStream out) {
        out.print(SET_BG_COLOR_YELLOW);
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