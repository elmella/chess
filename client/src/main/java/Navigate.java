import chess.ChessBoard;
import chess.ChessGame;
import websocket.ServerMessageHandler;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Scanner;

import static java.lang.System.out;
import static ui.DrawBoard.drawChessBoard;
import static ui.EscapeSequences.*;

public class Navigate implements ServerMessageHandler {

    private final Client client;

    public Navigate(String baseUrl) {
        client = new Client(baseUrl, this);
    }

    public void run() {
        out.println("Welcome to 240 chess. Type help to get started");

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            printPrompt(client.isLoggedIn(), client.isInGameplay());
            String input = scanner.nextLine();

            if (client.isInGameplay()) {
                try {
                    result = client.gamePlayMenu(input);
                    out.println(result);
                } catch (Throwable e) {
                    out.print(e.getMessage());
                }
            } else if (client.isLoggedIn()) {
                try {
                    result = client.loggedInMenu(input);
                    out.println(result);
                } catch (Throwable e) {
                    out.print(e.getMessage());
                }
            }
            else {
                try {
                    result = client.loggedOutMenu(input);
                    out.println(result);
                } catch (Throwable e) {
                    out.print(e.getMessage());
                }
            }
        }
        System.out.println();
    }

    private void printPrompt(boolean loggedIn, boolean gamePlay) {
        if (gamePlay) {
            System.out.println("[GAMEPLAY] >>>" );
        }
        else if(loggedIn) {
            System.out.println("[LOGGED_IN] >>>" );
        } else {
            System.out.println("[LOGGED_OUT] >>>" );
        }
    }


    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> displayNotification(((NotificationMessage) message).getMessage());
            case ERROR -> displayError(((ErrorMessage) message).getErrorMessage());
            case LOAD_GAME -> loadGame(((LoadGameMessage) message).getGame());
        }
    }


    private void displayNotification(String notification) {
        out.println(notification);
    }

    private void displayError(String error) {
        out.println(error);
    }

    private void loadGame(ChessGame game) {
        ChessBoard board = game.getBoard();
        board.resetBoard();
        out.print(ERASE_SCREEN);
        drawChessBoard(out, board, false);
        drawChessBoard(out, board, true);
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }
}
