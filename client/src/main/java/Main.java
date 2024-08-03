import chess.*;
import serverfacade.ServerFacade;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        ServerFacade s = new ServerFacade();
        String baseURL = "http://localhost:8080";

        String command = args[0];
        switch (command) {
            case "register":
                if (args.length != 4) {
                    System.out.println("Incorrect amount of arguments");
                    break;
                }

                try {
                    s.register(args[1], args[2], args[3], baseURL);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;

            case "login":
                if (args.length != 3) {
                    System.out.println("Incorrect amount of arguments");
                    break;
                }

                try {
                    s.login(args[1], args[2], baseURL);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }
}