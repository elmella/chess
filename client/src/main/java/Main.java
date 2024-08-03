import chess.*;
import serverfacade.ServerFacade;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to 240 chess. Type help to get started");
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.print("[LOGGED_OUT] >>> ");
            ServerFacade s = new ServerFacade();
            String baseURL = "http://localhost:8080";

            String command = args[0];
            switch (command) {
                case "help":
                    help(loggedIn);
                    break;
                    
                case "register":
                    if (args.length != 4) {
                        System.out.println("Incorrect amount of arguments");
                        break;
                    }

                    try {
                        System.out.println(s.register(args[1], args[2], args[3], baseURL));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    loggedIn = true;
                    break;

                case "login":
                    if (args.length != 3) {
                        System.out.println("Incorrect amount of arguments");
                        break;
                    }

                    try {
                        System.out.println(s.login(args[1], args[2], baseURL));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    loggedIn = true;
                    break;
            }
        }
    }

    private static void help(boolean loggedIn) {
        System.out.println(
                "register <USERNAME> <PASSWORD> <EMAIL> - to create an account" +
                "login <USERNAME> <PASSWORD> - to play chess" + "quit - playing chess"
                + "quit - playing chess"

        );
    }
}