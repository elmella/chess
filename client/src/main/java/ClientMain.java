import chess.BooleanWrapper;
import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import serverfacade.ServerFacade;
import websocket.ServerMessageHandler;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.out;
import static ui.DrawBoard.drawChessBoard;
import static ui.EscapeSequences.*;

public class ClientMain {
    public static void main(String[] args) {
        String baseUrl = "http://localhost:8080";
        if (args.length == 1) {
            baseUrl = args[0];
        }

        new Navigate(baseUrl).run();

    }
}


